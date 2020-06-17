(x, y, z, k, m, n, l) -> {
    StringBuilder sb = new StringBuilder(x.toUpperCase());
    sb.append(y.toUpperCase());
    sb.append(z.toUpperCase());
    sb.append(k.toUpperCase());
    sb.append(m.toUpperCase());
    sb.append(n.toUpperCase());
    sb.append(l.toUpperCase());
    
    String result = new String(sb);
    
    return result;
};
