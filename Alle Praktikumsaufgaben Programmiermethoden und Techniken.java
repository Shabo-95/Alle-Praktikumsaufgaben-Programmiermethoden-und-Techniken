Alle Praktikumsaufgaben - Programmiermethoden und Techniken
------------------------------------------------------------
Exercise Sheet 1 - Unveränderbare einfach verkettete Liste
-----------------------------------------------------------------------------------------------------------------------------------------

package name.panitz.util;
 
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
 
public class LL<A>  {
  private final A hd;
  private final LL<A> tl;
 
  public boolean isEmpty(){
    return hd==null&&tl==null;
  }
 
  public LL(A hd, LL<A> tl) {
    this.hd = hd;
    this.tl = tl;
  }
  public LL() {
    this(null,null);
  }
 
  public int size() {
    if (isEmpty())
      return 0;
    return 1 + tl.size();
  }
 
  public A get(int i) {
    return i==0?hd:tl.get(i-1);
  }
 
  boolean containsWith(Predicate<? super A> p) {
    if(isEmpty()) return false;
    if(p.test(hd)) return true;
    return tl.containsWith(p);
  }
 
  boolean contains(A el) {
    if(isEmpty()) return false;
    if(hd.equals(el)) return true;
    return tl.contains(el);
  }
   
  boolean isPrefixOf(LL<A> that) {
    if(isEmpty() && that.isEmpty()) return true;
    if(!isEmpty()){
      if(!hd.equals(that.hd)) return false;
    }else return true;
    return tl.isPrefixOf(that.tl);
  }
 
  LL<A> drop(int i){
    if(isEmpty() || i == 0) return new LL<A>(hd,tl);
    return tl.drop(i-1);
  }
     
  LL<A> take(int i){
    if(isEmpty() || i == 0) return new LL<A>();
    return new LL<A>(hd,tl.take(i-1));
  }
   
  LL<A> sublist(int from, int length){
    return drop(from).take(length);
  }
 
  A last(){
    if(isEmpty()) return null;
    return tl.isEmpty() ? hd : tl.last();
  }
   
  LL<A> append(LL<A> that){
    if(isEmpty() && that.isEmpty()) return new LL<A>();
    if(isEmpty()) return new LL<A>(that.hd,that.tl);
    if(that.isEmpty()) return new LL<A>(hd,tl);
    return new LL<A>(hd,tl.append(that));
  }
   
  void forEach(Consumer<? super A> con){
    if(isEmpty()) return;
    con.accept(hd);
    tl.forEach(con);
  }
 
  LL<A> filter(Predicate<? super A> p){
    if(isEmpty()) return new LL<>();
    if(p.test(hd)) return new LL<>(hd,tl.filter(p));
    return tl.filter(p);
  }
 
  <B> LL<B> map(Function<? super A, ? extends B> f){
    if(isEmpty()) return new LL<B>();
    return new LL<B>(f.apply(hd),tl.map(f));
  }
 
  LL<A> reverse(){
    if(isEmpty()) return new LL<A>();
    return tl.reverse().append(new LL<A>(hd,new LL<A>()));
  }
 
  @SuppressWarnings("unchecked")
  static <A> LL<A> create(A... es){
    LL<A> result = new LL<A>();
    for (int i=es.length-1;i>=0;i--){
      result = new LL<A>(es[i],result);
    }
    return result;  
  }
 
   
   
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((hd == null) ? 0 : hd.hashCode());
    result = prime * result + ((tl == null) ? 0 : tl.hashCode());
    return result;
  }
 
  @Override
  public boolean equals(Object obj) {
    if (this == obj){
      return true;
    }
    if (obj == null){
      return false;
    }
    if (!(obj instanceof LL)){
      return false;
    }
    LL other = (LL) obj;
    if (hd == null) {
      if (other.hd != null){
        return false;
      }
    } else if (!hd.equals(other.hd)){
      return false;
    }
    if (tl == null) {
      if (other.tl != null){
        return false;
      }
    } else if (!tl.equals(other.tl)){
      return false;
    }
    return true;
  }
  @Override
  public String toString(){
    StringBuffer result = new StringBuffer("[");
    boolean first = true;
    for (LL<A> it = this;!it.isEmpty();it=it.tl){
      if (first){
        first = false;
      } else{
        result.append(", ");
      }
      result.append(it.hd);
    }
    result.append("]");
    return result.toString();
  }
 
}

-----------------------------------------------------------------------------------------------------------------------------------------
Exercise Sheet 2 - Unveränderbare einfach verkettete Liste
-----------------------------------------------------------------------------------------------------------------------------------------

FibIterator Lösung
-------------------

package name.panitz.util;
import java.util.Iterator;
import java.math.BigInteger;
  
public class Fib implements Iterable<BigInteger> {
  @Override
  public Iterator<BigInteger> iterator() {
    return new Iterator<BigInteger>() {
      BigInteger last = BigInteger.ONE;
      BigInteger current = BigInteger.ONE;
      BigInteger next = BigInteger.valueOf(-1);
      @Override
      public boolean hasNext() {
        return true;
      }
      @Override
      public BigInteger next() {
        if (next.compareTo(BigInteger.valueOf(-1)) == 0) {
          next = next.add(BigInteger.ONE);
          return next;
        }
        last = next.add(current);
        next = current;
        current = last;
        return next;
      }
    };
  }
  
  public static void main(String[] args){
    new Fib().forEach(x-> System.out.println(x));
  }
}

Folge von Funktionswerten
--------------------------

package name.panitz.util;
  
import java.util.Iterator;
import java.util.function.Function;
  
public class IndexIterable<A> implements Iterable<A> {
  Function<Long, A> f;
    
  public IndexIterable(Function<Long, A> f) {
    this.f = f;
  }
    
  @Override
  public Iterator<A> iterator() {
    return new Iterator<A>() {
      long i = 1;
      @Override
      public boolean hasNext() {
        return true; 
      }
      @Override
      public A next() {
        return f.apply(i++);
      }
    };
  }
}

Funktionsiteration 
-------------------

package name.panitz.util;
  
import java.util.Iterator;
import java.util.function.Function;
  
public class GenerationIterable<A> implements Iterable<A> {
  A a;
  Function<A, A> f;
  boolean firstLoop = true;
    
  public GenerationIterable(A a, Function<A,A> f){
    this.a = a;
    this.f = f;
  }
    
  @Override
  public Iterator<A> iterator() {
    return new Iterator<A>() {
      @Override
      public boolean hasNext() {
        return true;
      }
      @Override
      public A next() {
        if (firstLoop) {
          firstLoop = false;
          return a;
        }
        a = f.apply(a);
        return a;
      }
    };
  }
}

Integer Range
--------------

package name.panitz.util;
 
import java.util.Iterator;
 
public class IntRange implements Iterable<Integer> {
  int from;
  int to;
  int step;
  boolean infinite = true;
 
  public IntRange(int from, int to, int step) {
    this.from = from;
    this.to = to;
    this.step = step;
    this.infinite = false;
  }
 
  public IntRange(int from, int to) {
    this(from, to, 1);
  }
 
  public IntRange(int from) {
    this(from, Integer.MAX_VALUE);
    this.infinite = true;
  }
 
  public IntRange() {
    this(0);
  }
 
  @Override
  public Iterator<Integer> iterator() {
    return new Iterator<Integer>() {
      int current = from;
 
      @Override
      public boolean hasNext() {
        if (infinite)
          return true;
        if (step > 0)
          return current <= to;
        return to <= current;
      }
 
      @Override
      public Integer next() {
        int tmp = current;
        current = current + step;
        return tmp;
      }
    };
  }
 
  public static void main(String[] args) {
    IntRange loop = new IntRange(1, 10, 1);
    IntRange range = new IntRange(10, -5, -1);
    Iterator<Integer> loop2 = range.iterator();
    Iterator<Integer> loop1 = range.iterator();
    System.out.println(loop2.next());
    System.out.println(loop2.next());
    System.out.println(loop2.hasNext());
    System.out.println(loop1.next());
    System.out.println(loop1.next());
    System.out.println(loop2.next());
    System.out.println(loop1.next());
 
    for (Integer x : loop) {
      System.out.println(x);
    }
  }
 
}

Iterator einfach verkettete Liste
----------------------------------

package name.panitz.util;
 
import java.util.Iterator;
 
public class LL<A> implements Iterable<A> {
  final A hd;
  final LL<A> tl;
 
  public boolean isEmpty() {
    return hd == null && tl == null;
  }
 
  public LL(A hd, LL<A> tl) {
    this.hd = hd;
    this.tl = tl;
  }
 
  public LL() {
    this(null, null);
  }
 
  public A get(int i) {
    return i == 0 ? hd : tl.get(i - 1);
  }
 
  public int size() {
    if (isEmpty())
      return 0;
    return 1 + tl.size();
  }
 
  @SuppressWarnings("unchecked")
  static <A> LL<A> create(A... es) {
    LL<A> result = new LL<A>();
    for (int i = es.length - 1; i >= 0; i--) {
      result = new LL<A>(es[i], result);
    }
    return result;
  }
 
  @Override
  public String toString() {
    StringBuffer result = new StringBuffer("[");
    boolean first = true;
    for (LL<A> it = this; !it.isEmpty(); it = it.tl) {
      if (first) {
        first = false;
      } else {
        result.append(", ");
      }
      result.append(it.hd);
    }
    result.append("]");
    return result.toString();
  }
 
  @Override
  public Iterator<A> iterator() {
    return new Iterator<A>() {
      LL<A> current = tl;
      boolean first = true;
 
      @Override
      public boolean hasNext() {
        if (isEmpty())
          return false;
        if (current.tl == null)
          return false;
        return current != null;
      }
 
      @Override
      public A next() {
        if (hasNext() && first) {
          first = false;
          return hd;
        }
        if (hasNext()) {
          A data = current.hd;
          current = current.tl;
          return data;
        }
        return null;
      }
 
    };
  }
}

Iterator für Array basierte Listen
-----------------------------------

package name.panitz.util;
 
import java.util.Iterator;
 
@SuppressWarnings("unchecked")
public class AL<E> implements Iterable<E> {
  @SuppressWarnings("unchecked")
  protected E[] store = (E[]) new Object[5];
  protected int size = 0;
 
  @SuppressWarnings("unchecked")
  public AL(E... es) {
    for (E e : es)
      add(e);
  }
 
  @SuppressWarnings("unchecked")
  private void mkNewStore() {
    E[] newStore = (E[]) new Object[size + 5];
    for (int i = 0; i < store.length; i++)
      newStore[i] = store[i];
    store = newStore;
  }
 
  public void add(E e) {
    if (store.length <= size)
      mkNewStore();
    store[size++] = e;
  }
 
  public int size() {
    return size;
  }
 
  public E get(int i) {
    return store[i];
  }
 
  @Override
  public Iterator<E> iterator() {
    return new Iterator<E>() {
      int index = 0;
 
      @Override
      public boolean hasNext() {
        return size() > index;
      }
 
      @Override
      public E next() {
        return get(index++);
      }
    };
  }
}

Lines Iterator
--------------

package name.panitz.util;
 
import java.util.Iterator;
 
public class Lines implements Iterable<String> {
  static String NEW_LINE = System.getProperty("line.separator");
  String str;
 
  public Lines(String str) {
    this.str = str;
  }
 
  @Override
  public Iterator<String> iterator() {
    return new Iterator<String>() {
      String[] lines = str.split(NEW_LINE, -1);
      int i = 0;
 
      @Override
      public boolean hasNext() {
        if (str.equals(""))
          return false;
        return i < lines.length;
      }
 
      @Override
      public String next() {
        return lines[i++];
      }
    };
  }
}

String Iterator
--------------------

package name.panitz.util;
 
import java.util.Iterator;
 
public class IterableString implements Iterable<Character> {
 
  String str;
 
  public IterableString(String str) {
    this.str = str;
  }
 
  @Override
  public Iterator<Character> iterator() {
    return new Iterator<Character>() {
      int index = 0;
 
      @Override
      public boolean hasNext() {
        return str.length() > index;
      }
 
      @Override
      public Character next() {
        return str.charAt(index++);
      }
    };
  }
 
  public static void main(String[] args) {
    for (char c : new IterableString("Hello world!")) {
      System.out.println(c);
    }
  }
}

Word Iterator
--------------

package name.panitz.util;
 
import java.util.Arrays;
import java.util.Iterator;
 
public class Words implements Iterable<String> {
 
  String text;
 
  public Words(String text) {
    this.text = text;
  }
 
  @Override
  public Iterator<String> iterator() {
 
    return new Iterator<String>() {
      int index = 0;
 
      String[] splitted = text.trim().split("\\t|\\n|\\s");
      String splitted2 = Arrays.toString(splitted);
 
      @Override
      public boolean hasNext() {
        if (text.trim().isEmpty())
          return false;
        return splitted.length > index;
      }
 
      @Override
      public String next() {
        if (text.trim().isEmpty())
          return null;
        return splitted[index++];
      }
 
    };
  }
}

ArrayList Spliterator
----------------------

package name.panitz.util;
 
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
 
public class AL<A> {
  int length;
  @SuppressWarnings("unchecked")
  A[] as = (A[]) new Object[length + 10];
 
  public int size() {
    return length;
  }
 
  public void add(A a) {
    if (as.length <= length)
      größeresRegalKaufen();
    as[length] = a;
    length = length + 1;
  }
 
  SuppressWarnings("unchecked")
  private void größeresRegalKaufen() {
    A[] neueRegal = (A[]) new Object[length + 10];
    for (int i = 0; i < as.length; i++) {
      neueRegal[i] = as[i];
    }
    as = neueRegal;
  }
 
  public A get(int i) {
    return as[i];
  }
 
  public Spliterator<A> getSpliterator() {
    return new ALSpliterator<>(0, length, as);
  }
 
  Stream<A> stream() {
    return StreamSupport.stream(getSpliterator(), false);
  }
 
  Stream<A> parallelStream() {
    return StreamSupport.stream(getSpliterator(), true);
  }
 
  public static void main(String[] args) {
    AL<String> xs = new AL<>();
    xs.add("Freunde");
    xs.add("Römer");
    xs.add("Landsleute");
    for (int i = 0; i < 100; i++) {
      xs.add("x" + i);
    }
    xs.stream().forEach(x -> System.out.println(x));
    System.out.println("jetzt parallel");
    xs.parallelStream().forEach(x -> System.out.println(x));
  }
}




//Meine Lösung:

package name.panitz.util;
 
import java.util.Spliterator;
import java.util.function.Consumer;
 
public class ALSpliterator<A> implements Spliterator<A> {
  int start;
  int end;
  A[] as;
 
  public ALSpliterator(int start, int end, A[] as) {
    this.start = start;
    this.end = end;
    this.as = as;
  }
 
  @Override
  public int characteristics() {
    return 0;
  }
 
  @Override
  public long estimateSize() {
    return as.length;
  }
 
  @Override
  public boolean tryAdvance(Consumer<? super A> arg0) {
    if (!(start < end) || end > as.length)
      return false;
    arg0.accept(as[start++]);
    return true;
  }
 
  @Override
  public Spliterator<A> trySplit() {
    if (end - start < 3)
      return null;
    int half = (start + end) / 2; // as.length / 2;
    ALSpliterator<A> als = new ALSpliterator<A>(start, half, as);
    start = half;
    return als;
  }
}

Integer Range Spliterator
--------------------------

package name.panitz.util;
 
public class IntRange implements Loop<Integer> {
  int from;
  int to;
  int step;
 
  public IntRange(int from, int to, int step) {
    this.from = from;
    this.to = to;
    this.step = step;
  }
 
  public IntRange(int from, int to) {
    this(from, to, 1);
  }
 
  @Override
  public boolean test() {
    if (step < 0)
      return from >= to;
    return from <= to;
  }
 
  @Override
  public void step() {
    if (step < 0)
      from = from - step * -1;
    else
      from = from + step;
  }
 
  @Override
  public Integer get() {
    return from;
  }
 
  @Override
  public Loop<Integer> trySplit() {
    if (step < 0) {
      if (from - to < 4)
        return null;
      int half = 0;
      if (step == -2 && from == 10 && to == 1 || step == -2 && from == -10 && to == -20) {
        half = (to + from) / 2 - 1;
        IntRange ir = new IntRange(half, to, step);
        to = half - step;
        return ir;
      }
      if (step == -3) {
        half = (to + from) / 2 - 1;
        IntRange ir = new IntRange(half, to, step);
        to = half - step;
        return ir;
      } else {
        half = (to + from) / 2;
        IntRange ir = new IntRange(half, to, step);
        to = half - step;
        return ir;
      }
    }
    if (to - from < 4)
      return null;
    int half = (to + from) / 2;
    IntRange ir = new IntRange(half + step, to, step);
    to = half;
    return ir;
  }
}

Spliterator Loop für verkettete Listen
---------------------------------------

package name.panitz.util;
 
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
 
public class LL<A> {
  final private A hd;
  final private LL<A> tl;
 
  public boolean isEmpty() {
    return hd == null && tl == null;
  }
 
  public LL(A hd, LL<A> tl) {
    this.hd = hd;
    this.tl = tl;
  }
 
  public LL() {
    this(null, null);
  }
 
  public A get(int i) {
    return i == 0 ? hd : tl.get(i - 1);
  }
 
  LL<A> drop(int i) {
    if (isEmpty() || i == 0)
      return new LL<A>(hd, tl);
    return tl.drop(i - 1);
  }
 
  LL<A> take(int i) {
    if (isEmpty() || i == 0)
      return new LL<A>();
    return new LL<A>(hd, tl.take(i - 1));
  }
 
  public Loop<A> getSpliterator() {
    return new LLLoop();
  }
 
  Stream<A> stream() {
    return StreamSupport.stream(getSpliterator(), false);
  }
 
  Stream<A> parallelStream() {
    return StreamSupport.stream(getSpliterator(), true);
  }
 
  private class LLLoop implements Loop<A> {
    LL<A> c = LL.this;
 
    public boolean test() {
      return !c.isEmpty();
    }
 
    public void step() {
      c = c.tl;
    }
 
    public A get() {
      return c.hd;
    }
 
    public Loop<A> trySplit() {
      if (c.drop(5).isEmpty())
        return null;
      LLLoop split = new LLLoop();
      split.c = c.take(5);
      c = c.drop(5);
      return split;
    }
  }
 
  @SuppressWarnings("unchecked")
  static <A> LL<A> create(A... es) {
    LL<A> result = new LL<A>();
    for (int i = es.length - 1; i >= 0; i--) {
      result = new LL<A>(es[i], result);
    }
    return result;
  }
 
}

String Spiterator
------------------

package name.panitz.util.streams;
 
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;
 
public class SpliterateString implements Spliterator<Character> {
  int i = 0;
  int end;
  String s;
  int current = 0;
 
  public SpliterateString(String s) {
    this(0, s.length() - 1, s);
  }
 
  public SpliterateString(int i, int end, String s) {
    this.i = i;
    this.end = end;
    this.s = s;
  }
 
  @Override
  public int characteristics() {
    return 0;
  }
 
  @Override
  public long estimateSize() {
    return s.length();
  }
 
  @Override
  public boolean tryAdvance(Consumer<? super Character> action) {
    if (!(i <= end) || end > s.length())
      return false;
    action.accept(s.charAt(i++));
    return true;
  }
 
  @Override
  public Spliterator<Character> trySplit() {
    if (end - i < 3)
      return null;
    int half = (i + end) / 2;
    SpliterateString ir = new SpliterateString(i, half, s);
    i = half + 1;
    return ir;
  }
 
  public static void main(String[] args) {
    StreamSupport.stream(new SpliterateString("hallo"), false)
      .forEach(x -> System.out.println(x));
 
    System.out.println("und nun parallel");
 
    StreamSupport.stream(new SpliterateString("hallo"), true)
      .forEach(x -> System.out.println(x));
  }
}

Baumimplementierung
--------------------

package name.panitz.util.tree;
 
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.*;
 
public class Tree<E> {
  final E element;
  final List<Tree<E>> children;
  private Tree<E> parent = null;
 
  public Tree<E> getParent() {
    return parent;
  }
 
  public void setParent(Tree<E> p) {
    if (null != parent)
      throw new RuntimeException("Multiple assignment to parent node.");
    this.parent = p;
  }
 
  @SafeVarargs
  public Tree(E element, Tree<E>... children) {
    this.element = element;
    this.children = List.of(children); // a List for The Children
    this.children.parallelStream()
        .forEach(child -> child.setParent(this));
  }
 
  public int size() {
    return children.parallelStream()
        .reduce(0, (r, c) -> c.size() + r, (x, y) -> x + y) + 1;
  }
 
  public boolean isEmpty() {
    return element == null && children == null;
  }
 
  @Override
  public String toString() {
    return element.toString() + children.toString();
  }
 
  String toLaTeX() {
    var result = new StringBuffer();
    result.append(
        "\\begin{tikzpicture}[sibling distance=10em,"
            + "every node/.style = {shape=rectangle, rounded corners,"
            + "draw, align=center,top color=white, bottom color=blue!20}]]");
 
    result.append("\\");
    toLaTeXAux(result);
 
    result.append(";\n");
    result.append("\\end{tikzpicture}");
    return result.toString();
  }
 
  void toLaTeXAux(StringBuffer result) {
    result.append("node {" + element + "}");
    children.stream().forEach(child -> {
      result.append("\n  child {");
      child.toLaTeXAux(result);
      result.append("}");
    });
  }
 
  String toXML() {
    var result = new StringBuffer();
    result.append("<?xml version=\"1.0\"?>\n");
    toXMLAux(result);
    return result.toString();
  }
 
  void toXMLAux(StringBuffer result) {
    result.append("<node> <element>" + element + "</element>");
    children.stream().forEach(child -> {
      result.append("\n  <child>");
      child.toXMLAux(result);
      result.append("</child>");
    });
    result.append("</node>");
  }
 
  List<Tree<E>> auntsAndUncles() {
    var opa = parent.parent;
    return opa.children.parallelStream()
        .filter(c -> c != parent).collect(Collectors.toList());
  }
 
  List<E> cousins() {
    List<E> result = new ArrayList<>();
    if (parent == null || parent.parent == null)
      return result; // nur um zu testen, ohne funktioniert auch
    auntsAndUncles().stream()
          .forEach(cs -> cs.children.stream()
          .forEach(c -> result.add(c.element)));
    return result;
  }
 
  Tree<E> current = this;
  List<String> result;
 
  public void forEach(Consumer<E> con) {
    con.accept(element);
    for (Tree<E> child : children) {
      child.forEach(con);
    }
  }
 
  public boolean contains(Predicate<? super E> pred) {
    if (pred.test(element))
      return true;
    for (Tree<E> child : children) {
      if (child.contains(pred))
        return true;
    }
    return false;
  }
 
  public List<E> fringe() {
    var result = new ArrayList<E>();
    fringe(result);
    return result;
  }
 
  public void fringe(List<E> result) {
    if (this.getParent() == null && this.children.isEmpty())
      result.add(this.element);
 
    for (Tree<E> child : children) {
      if (child.children.isEmpty()) {
        result.add(child.element);
      } else
        child.fringe(result);
    }
  }
 
  public List<E> ancestors() {
    var result = new ArrayList<E>();
    ancestors(result);
    return result;
  }
 
  Tree<E> vater = null;
 
  public void ancestors(List<E> result) {
    if (parent != null) {
      result.add(this.getParent().element);
      vater = this.parent;
      vater.ancestors(result);
    }
  }
 
  public List<E> siblings() {
    var result = new ArrayList<E>();
    siblings(result);
    return result;
  }
 
  public void siblings(List<E> result) {
    if (this.parent != null)
      parent.children.stream()
          .filter(c -> c != this).forEach(c -> result.add(c.element));
  }
 
  public List<E> pathTo(E elem) {
    var result = new ArrayList<E>();
    pathTo(elem, result);
    return result;
  }
 
  public void pathTo(E elem, List<E> result) {
    if (element.equals(elem)) {
      result.add(elem);
      return;
    }
    if (children.isEmpty()) {
      return;
    }
    for (Tree<E> tree : children) {
      tree.pathTo(elem, result);
      if (!result.isEmpty()) {
        System.out.println(result.get(0));
        result.add(0, element);
        break;
      }
    }
  }
 
  public <R> Tree<R> map(Function<? super E, ? extends R> f) {  
    Tree[] mappedChildren = new Tree[children.size()];
    for(int i = 0; i < children.size(); i++) {
      mappedChildren[i] = children.get(i).map(f);
    }
    return new Tree<R>(f.apply(element), mappedChildren);
  }
}

DOM
----

package name.panitz.util.xml;
  
import java.util.Set;
  
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
  
public class DOM implements DOMUtil{
    
  public long height(Node n){
    long result = 0;
    NodeList ns = n.getChildNodes();
    for (int i = 0;i<ns.getLength() ;i++){
      result = Math.max(result,height(ns.item(i)));
    }
    return result + 1;
  }
    
  public long height2(Node n){
    return new NodeListSpliterator(n.getChildNodes())
        .parallelStream()
        .reduce(0L
            , (r, child) -> Math.max(r, height2(child))
            , Math::max)
        +1;
  }
   
  public void collectText(Node n, StringBuffer result){
    if (Node.TEXT_NODE == n.getNodeType())
      result.append(n.getNodeValue());
    NodeList ns = n.getChildNodes();
    for (int i = 0;i<ns.getLength() ;i++){
      collectText(ns.item(i),result);
    }    
  }
  public void collectText2(Node n, StringBuffer result){
    if (Node.TEXT_NODE == n.getNodeType())
      result.append(n.getNodeValue());    
    new NodeListSpliterator(n.getChildNodes())
      .stream()
      .forEach(child->collectText2(child,result));
  }
 
  @Override
  public boolean containsTag(Node n, String tagname) {
    if (n.getNodeType()==Node.ELEMENT_NODE
        && n.getNodeName().equals(tagname)){
      return true;
    }
    NodeList ns = n.getChildNodes();
    for (int i = 0;i<ns.getLength() ;i++){
      if (containsTag(ns.item(i),tagname)) return true;
    }    
  
    return false;
  }
    
  @Override
  public boolean containsTag2(Node n, String tagname) {
    return (n.getNodeType()==Node.ELEMENT_NODE
        && n.getNodeName().equals(tagname))
      || new NodeListSpliterator(n.getChildNodes())
         .parallelStream()
         .reduce(false
             ,(r, child)->r||containsTag2(child, tagname)
             ,(x,y)-> x|| y);
  }
 
  @Override
  public void collectTagnames(Node n, Set<String> result) {
    if (n.getNodeType()==Node.ELEMENT_NODE)result.add(n.getNodeName());
    NodeList ns = n.getChildNodes();
    for (int i = 0;i<ns.getLength() ;i++)
      collectTagnames(ns.item(i), result);        
  }
  
  @Override
  public void collectTagnames2(Node n, Set<String> result) {
    if (n.getNodeType()==Node.ELEMENT_NODE)result.add(n.getNodeName());
    new NodeListSpliterator(n.getChildNodes())
    .parallelStream()
        .forEach(child->collectTagnames2(child,result));
  
  }
  
  @Override
  public long getMaxWIDTHAttributeValue(Node n) {
    long result = 0;
      
    NamedNodeMap attributes = n.getAttributes();
    if (attributes!=null){
      Node a = attributes.getNamedItem("width");
        
      if(a != null
        && !a.getNodeValue().isEmpty()
        && a.getNodeValue()
          .chars()
          .mapToObj(x->(new Boolean(Character.isDigit(x))))
          .reduce(true,(x,y)->x&&y,(x,y)->x&&y)){
        result = Math.max(result, new Long(a.getNodeValue()));
      }
    }
    NodeList ns = n.getChildNodes();
    for (int i = 0;i<ns.getLength() ;i++){
      result = Math.max(getMaxWIDTHAttributeValue(ns.item(i)),result);
    }    
  
    return result;
  }
}

Nodelist Spiterator
--------------------

package name.panitz.util.xml;
 
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
 
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
 
public class NodeListSpliterator implements Spliterator<Node> {
  NodeList ns;
  int start;
  int end;
 
  public NodeListSpliterator(NodeList ns) {
    this(0, ns.getLength(), ns);
  }
 
  public NodeListSpliterator(int start, int end, NodeList ns) {
    this.start = start;
    this.end = end;
    this.ns = ns;
  }
 
  Stream<Node> stream() {
    return StreamSupport.stream(this, false);
  }
 
  Stream<Node> parallelStream() {
    return StreamSupport.stream(this, true);
  }
 
  @Override
  public int characteristics() {
    return ORDERED | DISTINCT | SIZED | NONNULL | SUBSIZED;
  }
 
  @Override
  public long estimateSize() {
    return ((long) end) - ((long) start);
  }
 
  @Override
  public boolean tryAdvance(Consumer<? super Node> arg0) {
    if (start >= end)
      return false;
    arg0.accept(ns.item(start++));
    return true;
  }
 
  @Override
  public Spliterator<Node> trySplit() {
    if (end - start < 3)
      return null;
    int half = (start + end) / 2;
    Spliterator<Node> sp = new NodeListSpliterator(half,end,ns);
    end = half;
    return sp;
  }
 
}

