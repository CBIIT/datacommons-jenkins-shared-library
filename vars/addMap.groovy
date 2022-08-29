def merge(Map lhs, Map rhs) {
    return rhs.inject(lhs.clone()) { map, entry ->
        if (map[entry.key] instanceof Map && entry.value instanceof Map) {
            map[entry.key] = merge(map[entry.key], entry.value)
        } else if (map[entry.key] instanceof Collection && entry.value instanceof Collection) {
            map[entry.key] += entry.value
        } else {
            map[entry.key] = entry.value
        }
        return map
    }
}

def call(Map defaultMap,additionalMap){
    return merge(defaultMap,additionalMap)
}