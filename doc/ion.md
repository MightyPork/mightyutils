# ION

ION is a data storage library, mainly intended for saving games.

It uses a raw stream for writing/reading bytes, so you could use it both for files and ie. sockets.

Ion can write and load primitive types, their arrays, String, Collections, 
Maps, and any other types which are properly registered to the system.


## Why not just use serialization?

- YOU have complete control of what's going on, no magic
- Smaller file size
- Safe to change the data types while keeping old data files
- ION is cool


## Basic API

The main API class is called `Ion`, and provides a bunch of useful static methods, such as:

- `obj = Ion.fromFile(file)` - load an object from file
- `obj = Ion.fromStream(stream)` - load an object from stream
- `Ion.toFile(file, obj)` - save an object from file
- `Ion.toStream(stream, obj)` - save an object from stream
- `ionInput = Ion.getInput(file)` - get ion input (reading from a file)
- `ionOutput = Ion.getOutput(file)` - get ion output (writing to a file)

And there is more, check out the sources.

The actual writing and reading of objects is realized using `IonOutput` and `IonInput` which you can use as well - the methods on `Ion` are really just for convenience.


## Supported data types

Out-of-the-box, ION supports the following types:

### Built-ins & their arrays

- `boolean`
- `byte`
- `char`
- `short`
- `int`
- `long`
- `float`
- `double`
- `String`

### Map, Collection, arrays

- `Map` - if both key and value are of supported type
- `Collection` - if elements are of a supported type
- `IonBundle` - data holder, based on `Map<String, Object>`
- Array of any supported objects

## Adding custom data types

### Marks

Ion uses byte marks to identify all the supported objects. Possible values for 
a mark are 0..255, where values 0..49 are reserved for internal use.

Therefore, you can use the remaining values (50..255) for custom data objects.

### Creating a data type

First, let your data class implement either `IonObjBinary` or `IonObjBundled`, and make sure it has a *implicit constructor* - that is, with no arguments. You can have other constructors as well, that's okay.

**Binary vs Bundled**

- **IonObjBinary**
  - uses directly `IonOutput` and `IonInput` for saving and loading
  - the data fields can be written without marks, since the implementing class knows what is where
  - that means smaller file size, but it's less portable (if you add a new data field, the old saves will be corrupted)

- **IonObjBundled**
  - uses `IonBundle` which is automatically saved and loaded by `IonOutput` and `IonInput`
  - that means you can safely add new data fields to such an object, and migrating from old saves will work without problems
  - the drawback is a considerably bigger file size, since the string keys have to be stored as well as the values (of course, using short keys is beneficial here).


### Registering new data type

Once you have the data type implemented, you can register it.

You have two options:

- Use `Ion.register(mark, objClass)`, where
  - _mark_ is a byte mark to be used
  - _objClass_ is a class of the registered object
  
- Use `Ion.register(objClass)` and define the mark via a constant in the registered type:<br>
  `public static final int ION_MARK = <something>;`



## Behind the scenes - how it works

### How ion saves/loads the data

This is very simplified, but you should et the general idea. For better info, read the source, it's pretty well commented via JavaDoc.

**Saving:**

1. `IonOutput` looks into a registry what mark the object has
2. It writes this mark into the stream
3. It writes the object to the stream

**Loading:**

1. `IonInput` reads a mark, and finds a class associated with that mark.
2. It creates an instance using the implicit constructor (if it's not a primitive type)
3. It loads the object from the stream

### Data structures

**Arrays** are typically done by writing array mark, then length of the array, and then the elements one-by-one without marks (unless they are of different type).

```
ARRAY (specific for each type)
...length (int)...
...all the elements, without marks...
```

An exception is an Object array, where each entry also contains the type mark (to distinguish them while loading).

**Collection** is written like this - reading is simply a loop until the next mark is END instead of ENTRY.

```
SEQUENCE
ENTRY
<data type mark> // element
...entry data...
ENTRY
<data type mark> // element
...entry data...
END
```

**Map** is done the same way as collection, except each entry contains both key and the value.

```
MAP
ENTRY
<data type mark> // key
...entry data...
<data type mark> // value
...entry data...
ENTRY
<data type mark> // key
...entry data...
<data type mark> // value
...entry data...
END
```
