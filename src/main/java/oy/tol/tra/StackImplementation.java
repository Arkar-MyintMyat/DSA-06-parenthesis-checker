package oy.tol.tra;

/**
 * An implementation of the StackInterface.
 */
public class StackImplementation<E> implements StackInterface<E> {

   private Object [] itemArray;
   private int capacity;
   private int currentIndex = -1;
   private static final int DEFAULT_STACK_SIZE = 10;

   /**
    * Allocates a stack with a default capacity.
    * @throws StackAllocationException
    */
   public StackImplementation() throws StackAllocationException {
      this(DEFAULT_STACK_SIZE);
   }

   /**
    * @param capacity The capacity of the stack.
    * @throws StackAllocationException If cannot allocate room for the internal array.
    */
   public StackImplementation(int capacity) throws StackAllocationException {
      if (capacity < 2) {
         throw new StackAllocationException("Stack capacity must be at least 2, was " + capacity);
      }
      try {
         itemArray = new Object[capacity];
      } catch (OutOfMemoryError e) {
         throw new StackAllocationException("Could not allocate room for a stack of capacity " + capacity);
      }
      this.capacity = capacity;
      currentIndex = -1;
   }

   @Override
   public int capacity() {
      return capacity;
   }

   @Override
   public void push(E element) throws StackAllocationException, NullPointerException {
      if (element == null) {
         throw new NullPointerException("Cannot push null to the stack");
      }
      if (currentIndex + 1 >= capacity) {
         reallocate();
      }
      itemArray[++currentIndex] = element;
   }

   @SuppressWarnings("unchecked")
   @Override
   public E pop() throws StackIsEmptyException {
      if (isEmpty()) {
         throw new StackIsEmptyException("Cannot pop from an empty stack");
      }
      E element = (E) itemArray[currentIndex];
      itemArray[currentIndex--] = null;
      return element;
   }

   @SuppressWarnings("unchecked")
   @Override
   public E peek() throws StackIsEmptyException {
      if (isEmpty()) {
         throw new StackIsEmptyException("Cannot peek an empty stack");
      }
      return (E) itemArray[currentIndex];
   }

   @Override
   public int size() {
      return currentIndex + 1;
   }

   @Override
   public void clear() {
      itemArray = new Object[DEFAULT_STACK_SIZE];
      capacity = DEFAULT_STACK_SIZE;
      currentIndex = -1;
   }

   @Override
   public boolean isEmpty() {
      return currentIndex < 0;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder("[");
      for (var index = 0; index <= currentIndex; index++) {
         builder.append(itemArray[index].toString());
         if (index < currentIndex) {
            builder.append(", ");
         }
      }
      builder.append("]");
      return builder.toString();
   }

   private void reallocate() throws StackAllocationException {
      if (capacity == Integer.MAX_VALUE) {
         throw new StackAllocationException("Stack cannot grow beyond its maximum capacity");
      }
      int newCapacity = capacity > Integer.MAX_VALUE / 2 ? Integer.MAX_VALUE : capacity * 2;
      Object [] newArray;
      try {
         newArray = new Object[newCapacity];
      } catch (OutOfMemoryError e) {
         throw new StackAllocationException("Could not allocate room for a stack of capacity " + newCapacity);
      }
      for (int index = 0; index <= currentIndex; index++) {
         newArray[index] = itemArray[index];
      }
      itemArray = newArray;
      capacity = newCapacity;
   }
}
