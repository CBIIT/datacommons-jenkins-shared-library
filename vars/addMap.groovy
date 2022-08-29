def call(Map defaultMap,Map additionalMap){
    def finalMap = [:];
    finalMap.putAll( defaultMap )
    finalMap.putAll( additionalMap );
    finalMap.each { key, value ->
        if( defaultMap[key] && additionalMap[key] )
        {
            finalMap[key] = defaultMap[key] + additionalMap[key]
        }
    }
    return finalMap
}
