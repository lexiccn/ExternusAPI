# Animation API 

The animation API is a helpful tool for making animations ingame. An animation is composed of many frames. Over time the animation will run each frame in sequence. Each frame contains a bit of information for the animation to draw it. In a traditional animation such as a GIF each frame consists of the pixels that need to be displayed to the screen and each frame is played on a regular basis. 

The animation API follows similar principles except for the fact that the animation may be played on many different kinds of "screens" such as to a Player or to the World. The information to be displayed is not pixels but is instead numbers, items or particle effects.

## Creating an animation

Once again this wiki tutorial will be done with an example. In this example we will be creating an animation which displays lines of text to a sender.

### Animation Renderer

The animation renderer describes how to turn the information into something on the screen. In this case we are going to describe how to print a string to a senders screen. 

```java
public class ChatAnimationRenderer implements AnimationRenderer<String, Sender> {
    @Override
    public void render(@NotNull RunningAnimation<Sender> runningAnimation, @NotNull MinecraftFrame<String> minecraftFrame, @NotNull Sender sender) {
        //render the animation information to the "screen". For our case this means printing the message to the users chat. 
        sender.sendMessage(minecraftFrame.getObject());
    }

    @NotNull @Override
    public AnimationRenderer<String, Sender> getNewRenderer() {
        //this should return a new chat animation as if it were first being created. This essentially acts as a factory
        //method.
        return new ChatAnimationRenderer();
    }
}
```

### Minecraft Animation

Once you have created the animation renderer you can now create an animation with the Minecraft Animation class. The minecraft animation is your 'goto' platform for starting and stopping animations. When creating a minecraft animation you must specify the animation factory. This essentially determines how the running animation will function, i.e whether it will be sync or async. For async animation you usually use schedule async.

```java
MinecraftAnimation<String,Sender> animation = new MinecraftAnimation<>(
        this, 
        AnimationFactories.SCHEDULE_ASYNC(), //Create an async animation.
        new ChatAnimationRenderer());
```

#### Adding Frames

To properly run the animation we need to add some frames. Each frame is run x ms after the previous frame. The frames will be played in the order in which they were added. 

```java
animation.addFrame(new MinecraftFrame<>("Hello",30)); //this will run 30ms after the animation started
animation.addFrame(new MinecraftFrame<>("my",70)); //this will run 70ms after the previous frame
animation.addFrame(new MinecraftFrame<>("name",80)); //this will run 80ms after the previous frame
animation.addFrame(new MinecraftFrame<>("is",1000)); //this will run 1 second after the previous frame
animation.addFrame(new MinecraftFrame<>("DeltaOrion",2, ChronoUnit.SECONDS)); //this will run 2 second after the previous frame
```

## Running Animations 

A `MinecraftAnimation` simply holds information about an animation. To run it you must create a running animation. A `RunningAnimation` represents an animation that is running and their viewers. A running animation can be stopped, started or even have its speed changed.

```java
Sender sender = getEServer().getSenderExact("DeltaOrion");
RunningAnimation<Sender> runningAnimation = animation.start(sender);
```

#### Running animations simultaneously

We can optionally run many animations at the same time 

```java
Sender sender = getEServer().getSenderExact("DeltaOrion");
for(int i=0;i<100;i++){
    animation.start(sender); //this might get a little dizzy. 
}
```

#### Adding more Screens

We can also add many screens to an animation. We can even add screens 

```java
//get all online senders
Collection<Sender> senders = getEServer().getOnlineSenders();
//start the chat animation to everyone online.
RunningAnimation<Sender> runningAnimation = animation.start(senders);
//add the console after the animation started
runningAnimation.addScreen(getEServer().getConsoleSender());
```

### Other Manipulations

#### Cancellation

Note once a running animation has been cancelled it cannot be started up again for any reason. If the animation is set to "restart" it will still not resart. 

```java
RunningAnimation<Sender> runningAnimation = animation.start(senders);
runningAnimation.cancel(); //cancels the animation
```

#### Pausing & Playing

The following will pause an animation and start it back up again. It is highly recomended to clean up stalled animations on plugin disable. 



```java
runningAnimation.pause();
runningAnimation.play();
```

#### Playback Speed

This will affect the rate at which frames are played. If the play speed is set to 2.0 the animation will run 2x as fast. 

```java
runningAnimation.setPlaySpeed(0.5f); //sets the animation to half speed
runningAnimation.setPlaySpeed(2.0f); //sets the animation to double speed
```

## Self-Repeating Animation

Once an animation has played all of its frames it will normally temrinate. This can be prevented however by overriding the onComplete method in the AnimationRenderer. If BeforeCompletion returns true then the animation will restart instead of terminating. If an animation is cancelled using `RunningAnimation#cancel()` it will not be restarted even if beforeCompletion returns true. 
```java
int counter = 0;
//The animation will repeat 3 times before being canceled.
@Override
public boolean beforeCompletion(@NotNull RunningAnimation<Sender> animation) {
    //this code will be run everytime the animation completes. 
    if(counter<3) {
        //send a message to all the screens in the animation that it is restarted
        for(Sender screen : animation.getScreens()) 
            screen.sendMessage("Restarting Animation!");
        
        counter++;
        //by returning true the animation is restarted    
        return true;
    }
    //returning false will ensure the animation terminates. 
    return false;
}
```


