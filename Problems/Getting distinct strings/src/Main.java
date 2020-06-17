x -> {
    HashSet<String> distinctStrings = new HashSet<>();
    
    for (String str : x) {
        distinctStrings.add(str);
    }
    
    return new ArrayList<>(distinctStrings);
};
