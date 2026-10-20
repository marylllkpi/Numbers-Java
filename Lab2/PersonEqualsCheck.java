public class PersonEqualsCheck {
    public static void main(String[] args) {
        Person a = new Person("Lutsenko", "Maria", 20);
        Person b = new Person("Lutsenko", "Maria", 20);
        Person x = new Person("Lutsenko", "Maria", 20);
        Person c = new Person("Lutsenko", "Maria", 21);
        Person d = new Person("Petrenko", "Maria", 20);
        Person e = new Person("Lutsenko", "Iryna", 20);

        System.out.println("Reflexive    a.equals(a):               " + a.equals(a));
        System.out.println("Symmetric    a.equals(b) == b.equals(a): " + (a.equals(b) == b.equals(a)));
        System.out.println("Transitive   a=b, b=x => a=x:           " + (a.equals(b) && b.equals(x) && a.equals(x)));
        System.out.println("equals(null) is false:                  " + !a.equals(null));
        System.out.println("equals(other type) is false:            " + !a.equals("text"));
        System.out.println("Different age -> not equal:             " + !a.equals(c));
        System.out.println("Different last name -> not equal:       " + !a.equals(d));
        System.out.println("Different first name -> not equal:      " + !a.equals(e));
        System.out.println("Equal objects -> same hashCode:         " + (a.hashCode() == b.hashCode()));
        System.out.println("null fields do not break equals:        "
                + new Person(null, null, 0).equals(new Person(null, null, 0)));
    }
}
