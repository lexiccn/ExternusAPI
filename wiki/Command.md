# Command API

The command API focuses around the creation of large nested tree commands and their tab completion. To demonstrate command creation we will use a simple example command.

A command is broken into two logic units. First a `SentCommand` which represents a command sent by a user (what the user typed) and a `FunctionalCommand` which is a class that tells the server what to do with the command that was sent. 

When a user sends a command it will have arguments `/command arg1 arg2`. In a `SentCommand` an argument is just a string however in a `FunctionalCommand` an argument can either represent a piece of logic that needs to be run or a whole other command. 

```text
Note- this is not YAML or anything it is just to show the structure of our command!
TestCommand:
    a: prints 'a'
        a1: prints 'a1'
        a2: prints 'a2'
            a3: prints the users name 
    b: adds the two arguments
    
    For example
     - /TestCommand a a2 a3 should print 'a3'.
     - /TestCommand a should print 'a'
     - /TestCommand b 1 3 should print 4
     
Lastly 
  - if TestCommand is run without a valid argument we want to show a help menu
```

To begin making a command make a class as follows
```java
public class TestCommand extends FunctionalCommand {
    protected TestCommand() {
        super("MyPlugin.TestCommand","/TestCommand ?", Message.valueOf("Does some stuff!"));
    }
    
    @Override
    public void commandLogic(SentCommand sentCommand) throws CommandException {
        //I will be run if the user types /TestCommand [argument that is not registered]
        //For example 
        //   - /TestCommand
        //   - /TestCommand c 3 4
    }
}
```

Now we want to begin registering arguments. For our example the argument `a` represents a sub-command because it has its own arguments, a1 and a2. The command b however simply represents some logic. Every time you register an argument it will also register tab completion for that argument!

```java
import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.FunctionalCommand;
import me.deltaorion.common.command.sent.SentCommand;
import me.deltaorion.common.locale.message.Message;

public class TestCommand extends FunctionalCommand {
    protected TestCommand() {
        super("MyPlugin.TestCommand","/TestCommand ?", Message.valueOf("Does some stuff!"));
        registerArguments();
    }

    private void registerArguments() {
        registerArgument("a",new ACommand());
        registerArgument("b",this::doB);
    }

    @Override
    public void commandLogic(SentCommand sentCommand) throws CommandException {
        //I will be run if the user types /TestCommand [argument that is not registered]
        //For example 
        //   - /TestCommand
        //   - /TestCommand c 3 4
    }

    public void doB(SentCommand command) {
        //I will be run when the user types /TestCommand b
    }

    public static class ACommand extends FunctionalCommand {
        protected ACommand() {
            super("MyPlugin.Command.A");
        }

        @Override
        public void commandLogic(SentCommand sentCommand) throws CommandException {
            //I will be run when the user does /TestCommand a [argument that is not registered]
            
        }
    }
}
```

Now we can begin filling in the logic

```java
import me.deltaorion.common.command.Command;
import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.FunctionalCommand;
import me.deltaorion.common.command.sent.SentCommand;
import me.deltaorion.common.locale.message.Message;

import java.util.Map;

public class TestCommand extends FunctionalCommand {
    protected TestCommand() {
        super("MyPlugin.TestCommand","/TestCommand ?", Message.valueOf("Does some stuff!"));
        registerArguments();
    }

    private void registerArguments() {
        registerArgument("a",new ACommand());
        registerArgument("b",this::doB);
    }

    @Override
    public void commandLogic(SentCommand sentCommand) throws CommandException {
        //I will be run if the user types /TestCommand [argument that is not registered
        sentCommand.getSender().sendMessage("---oO TestCommand Oo---");
        for(Map.Entry<String, Command> entry : getFunctions().entrySet()) {
            //note getLabel() sends what the user typed to get the command. In this case /TestCommand
            sentCommand.getSender().sendMessage("/" +sentCommand.getLabel() + " " + entry.getKey() + ": "+entry.getValue().getDescription());
        }
    }

    public void doB(SentCommand command) throws CommandException {
        //I will be run when the user types /TestCommand
        String errMessage = command.getLabel() + " b [num1] [num2]";
        //this will get the first argument after the b as an int
        //if the sender did not provide this argument this will throw a command exception.
        int a = command.getArgOrFail(0,errMessage).asInt();
        int b = command.getArgOrFail(1,errMessage).asInt();
        int sum = a+b;
        command.getSender().sendMessage("Sum: "+sum);
    }

    public static class ACommand extends FunctionalCommand {
        protected ACommand() {
            super("MyPlugin.Command.A");
            registerArgument("a1",c -> {
                c.getSender().sendMessage("a1");
            });

            registerA2();
        }

        private void registerA2() {
            //the command builder can be a nice shorthand and less-verbose way of making a subcommand
            //rather than simply making a new class!
            registerArgument("a2",new FunctionalCommand.Builder()
                    .onCommand(c -> {
                        c.getSender().sendMessage("a2");
                    }).addArgument("a3",builder -> {
                        builder.onCommand(c -> {
                            c.getSender().sendMessage(c.getSender().getName());
                        });
                    }).assertNoPermission().build());
        }

        @Override
        public void commandLogic(SentCommand sentCommand) throws CommandException {
            sentCommand.getSender().sendMessage("a");
        }
    }
}
```

Lastly you MUST register the command! Do NOT bother with any plugin.yml's 

```java
@Override
public void onPluginEnable() {
    registerCommand(new TestCommand(),"TestCommand","Tcommand","Tc");
}
```

### Command Exception

Command exception is a handy exception that should be thrown if the predicates for a command are not run. If thrown this will instantly interrupt command execution and send the given message to the command sender.

```java
public class TestCommand extends FunctionalCommand {
    protected TestCommand() {
        super("MyPlugin.TestCommand","/TestCommand ?", Message.valueOf("Does some stuff!"));
    }
    
    @Override
    public void commandLogic(SentCommand sentCommand) throws CommandException {
        //If the sender is DeltaOrion they will be sent the message "BE GONE". Because 
        //a command exception was thrown the line after wont be run. 
        if(sentCommand.getSender().getName().equals("DeltaOrion"))
            throw new CommandException("BE GONE!");
        
        
        sentCommand.getSender().sendMessage("You shall pass!");
    }
}
```

### How to get Bukkit Player

```java
@Override
public void commandLogic(SentCommand sentCommand) throws CommandException {
    //check if the sender is console
    if(sentCommand.getSender().isConsole())
        throw new CommandException("Only players may use this command!");

    //this will get the player from the sender.
    Player playerSender = Bukkit.getPlayer(sentCommand.getSender().getUniqueId());
    
    //this will return the player from the argument. So if they typed in "/label DeltaOrion" this will get DeltaOrion
    Player playerArgument = sentCommand.getArgOrFail(0).parse(Player.class);
}
```

### Tab Completion

As noted earlier argument tab completion is already done for you!. However for more
advanced tab completion you will still need to tell the server what to do. 

```java

```




