def call(Map config){
    post("define post build actions"){
	    cleanup {

            cleanWs()

        }
    }
}