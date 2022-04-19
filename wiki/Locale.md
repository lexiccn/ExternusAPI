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

### PlaceHolders

