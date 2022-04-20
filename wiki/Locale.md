# Locale Library 

The locale library allows you to send messages to players in their language and to render messages for a given locale.  

The locale library has two main parts

  - The Translator
  - Messages 

A given message, say "Hello" will be different for every single language. In french it might be "Bonjour" or in english "Guten Tag". To identify that we are trying to say Hello we mark that message with a key. In this case the key could be named something like 'greeting'.  

## Translator
The translator is a class that links each Locale to a dictionary of key to translation. To load all of the translations from their respective files to the Translator the API uses a class called a  `TranslationManager`. This class takes care of most of the file reading for you. 

To load locales from the files do the following

### Default Locale 

Create a default locale file called `en.properties` that is located in the base of the plugins resource folder (the same place where the plugin.yml is). This stores all of the default english translations

```properties
key=translation
greeting=Hello
```

### Custom Locale's 

Add any extra translations in the plugins data folder `%Server%/plugins/MyPlugin/translations/`.  Each translation should be called `%ISO_2Digit%.properties`.

For example if you wanted french as a translation your file would be `fr.properties`. If you wanted english pirate speak your file would be `en_PT.properties`.

How you put the translation files into the plugin data folder is up to you whether you download them from CrowdIn or you take them from the plugins resource folder. 

If you add another en.properties into the translations then the file in the translations file will take precedent. 

The content of the custom locale file is the same as the default locale. For example if we were to make a fr.properties it would look as follows

```properties
key=Traduction
greeting=Bonjour
```

## Message

A message is a class that represents a message that can be sent to a player whether it is translatable or not. For translations to work you must load locales into the translator as shown above.

### Translatable Message

This will create a message that represents the key "greeting"
```java
Message message = Message.valueOfTranslatable("greeting");
```

Because 'greeting' is different for every language, to convert it to a string we need to specify for which language to convert it to

```java
Message message = Message.valueOfTranslatable("greeting");
//this will render the string in english which should output 'Hello'
String rendered = message.toString(Locale.ENGLISH);
```

### Simple Message

Sometimes we dont want to represent a message as a translatable and instead as just a primitive such as a string. In this case you can do it like this

```java
Message message = Message.valueOf("Gamer");
String gamer = message.toString();
```

### Message Builder

There may also be times where you might want to combine many translations and simple messages together. In this case the MessageBuilder should be used. 

```java
//this makes a message with the greeting translatable surrounded by brackets.
Message message = Message.valueOfBuilder(builder -> {
    builder.append("[")
        .appendTranslatable("greeting")
        .append("]");
});
//For the english translation this would return "[Hello]"
System.out.println(message.toString());
```

## Placeholders

With messages sent to players it is an extremely common task to put values such as names or numbers in the middle of them. For example a message that might display the players hearts might be "The player DeltaOrion has 4/10 health". In this case the number 4 will change depending on their health and the playername depending on the player. To represent this we use Placeholders. 

### Adding Placeholders

Placeholders are represented as a {num} where num is the number of the placeholder. For example, {0} represents the first argument entered, {1} the second, {2} the third etc etc. The syntax for adding a placeholder is the same for the java and the language properties files. 

#### Java

```java
Message playerHealth = Message.valueOf("The player {0} has {1}/{2} health");
```

#### Properties

```properties
player-health=The player {0} has {1}/{2} health
```

### Rendering placeholders

To render in the actual values of the placeholders simply do the following inside of your java code. 

```java
//You can always use a translatable as well!
Message playerHealth = Message.valueOf("The player {0} has {1}/{2} health");
                                 //{0}                  {1}                   {2}
String render = message.toString(Locale.ENGLISH,player.getName(),player.getHealth(),player.getMaxHealth());
```

### Repeated Placeholders

In your properties file or java code you may repeat placeholders as many times as you wish. 

```properties
key=I like repeating myself {0} and again {0} and again {0}
```
The first argument entered in the `toString()` will be substituted into all 3 `{0}`

### Escaping Placeholders

If for whatever reason you want to display a placeholder instead of having a value substituted into it you can escape it as follows

```properties
explain-placeholder=Placeholders can be added into a message using {num} where \\{0} is the first argument entered \\{1} is the second...
```

When rendered by as follows

```java
Message message = Message.valueOfTranslatable("explain-placeholder");
//this will output
//Placeholders can be added into a message using {num} where {0} is the first argument entered {1} is the second...
//because the placeholders are escaped.  
System.out.println(message.toString("hack","put me in!"));
```