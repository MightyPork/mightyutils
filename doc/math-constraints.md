# Dynamic constraints - DynMath library

DynMath is a library aimed at making GUI positioning and sizing easier.

It allows you to compose numeric, vector and rectangle constraints for the GUI elements, and when some of the input values changes (such as window size), all the constraints will reflect this change.

For example:

```java
// a Rect representing the whole window
Rect window = new Rect() {
	
	@Override
	public Vect size()
	{
		return /* actual window size */;
	}
	
	@Override
	public Vect origin()
	{
		return Vect.ZERO;
	}
};

// navbar in the bottom 20% of the window
Rect navbar = window.bottomEdge().growUp(window.height().perc(20));

// now whenever the window is resized, navbar will have the right size
```

The library is, however, very general-purpose, and has many other uses besides the GUI positioning.

## What is available

Short answer: A LOT!

Long answer:

### Constraint Types

There are three kinds of "constraints":

- `Num` - a number (or a 1D vector)
- `Vect` - 2D or 3D vector, can be used as absolute or relative coordinate
- `Rect` - a 2D rectangle composed of _origin_ and _size_ `Vect`

Each of them has a bunch of different variants, and can also be further extended to suit your needs. For example, you could make Gui components extend `AbstractRectCache` or something like that.

Generally, all the constraints are immutable (except the mutable variants, such as `VectVar`.

### Making Constraints

Each `Vect`, `Rect` and `Num` have a lot of factory methods (`.make()`, `.makeVar()` etc, with various argument types) to cover all the most common scenarios. If none of those is what you want, you can just make a new instance of the respective constraint, and make it fit your needs.

### Variants of Constraints
Just `Num`, `Vect` and `Rect` would be boring.

Each of them has (at least) the following variants *(it'll be shown on `Vect`)*:

- `Vect` - the basic abstract type. Can be extended to make dynamic variables (override the `.x()`, `.y()` (and optionally `.z()`) methods with your own calculations). `Vect` is generally dynamic, thus doing math on `Vect` will give you "view" of the result, which changes as the original `Vect` changes.

  Note, that `Vect` uses `.x()`, `.y()` and `.z()`, `Rect` uses `.origin()` and `.size()`, and `Num` uses `.value()`. Each variant of the constraints overrides those methods in a different way.
- `VectConst` - concrete implementation of `Vect`, backed by actual numbers. As the name suggests, it is immutable and keeps the same value. You can obtain a const from ie. `Vect` by calling it's `.freeze()` method.
- `VectVar` - like const, it is backed by actual numbers, but they are not final and it has setters to change their values. This could be used as a input for computation.
- `VectAdapter` - kinda like a proxy with abstract method to provide the proxied `Vect`.
- `VectProxy` - implementation of `VectAdapter`, that keeps the proxied constraint in a variable and this can be changed using the `.setVect()` method.
- `VectCache` (and `AbstractVectCache`) - useful if you have a very complex calculation and don't want it all to be evaluated each time you want the result. Simply call the `.cached()` result on your formula, and store it into a `VectCache` variable. Each time you call it's `.poll()` method, it will copy current value into an internal `VectVar` (see, that is what they're good for), and this is then used to get the result.
- `VectBound` - okay, this is not really a variant. It is an interface for **anything** that can has a `Vect` value. You can use `VectBoundAdapter` to make an actual `Vect` out of it.

### Digests

If you use some constraint often, like VERY often, ie. for OpenGL rendering each frame, it's a good idea to use digests. The important part is that the digest is cached until the value changes, so it's very efficient if you need the digest often.

All of the constraints support taking digests.

Steps to enable and use digest caching:

1. Call `.enableDigestCaching(true)` on your constraint to turn on the feature (cache does this by default)
2. Each time you need a digest, call the `.digest()` method on your constraint.
3. Each time the value is expected to have changed, call `.markDigestDirty()` on it, and the digest will be rebuilt next time it is needed. Cache does this on each `.poll()` automatically for you.
