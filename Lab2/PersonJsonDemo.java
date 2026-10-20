public class PersonJsonDemo {
    public static void main(String[] args) {
        Person original = new Person("Lutsenko", "Maria", 20);   
        String json = PersonJson.toJson(original);              
        Person restored = PersonJson.fromJson(json);            

        System.out.println("Original: " + original);
        System.out.println("JSON:     " + json);
        System.out.println("Restored: " + restored);
        System.out.println("original.equals(restored) = " + original.equals(restored)); 
    }
}
