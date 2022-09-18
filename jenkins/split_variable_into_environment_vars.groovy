'''
Description:
  This is a paramaterized pipeline which converst a variable into multiple environment variables.
  It expects a parameter `LONG_STRING_COLON_DELIMITED` which is a sequence of key=values separate by a colon 

Reference Links :
 - https://stackoverflow.com/a/9915250/9454903
 - https://stackoverflow.com/a/68391390/9454903
 - https://stackoverflow.com/a/52188510/9454903
'''

keys=[]
def createEnv() {
    // def data = "key1=val1;key2=val2;key3=val3"
    def data = "${env.LONG_STRING_COLON_DELIMITED}"
    data.split(';').each { entry -> 
        println(entry)
        def (key, value) = entry.split('=')
        env."$key"=value        // HERE IS WHAT COMMON MISTAK WE DO, always quote the `key` with a dollar sign
        
        // Maintain a list of all the keys to print later on
        keys.add(key)
    }
}

pipeline{
    agent{
        label 'master'
    }
    parameters {
        string(name: 'LONG_STRING_COLON_DELIMITED', defaultValue: 'FIRST_VAR=First var value;SECOND_VAR=2nd var value', description: 'FIRST_VAR=First var value;SECOND_VAR=2nd var value')
    }
    stages{
        // To avoid getting error for empty value passed
        stage('Convert the Variable into ENV Variable'){
            steps{
                script{
                    createEnv()
                }
            }
        }
        stage('Print All Vars'){
            steps{
                echo "${env.FIRST_VAR}"
                sh 'env | grep _VAR'
                script{
                    
                    echo "****************"
                    println("My long string of key=value separate by a colon ---> ${LONG_STRING_COLON_DELIMITED}")
                  
//                     println("FIRST_VAR --> ${env.FIRST_VAR}")
//                     println("SECOND_VAR --> ${env.SECOND_VAR}")
                    
                    print("keyss --> $keys")
                    def envs = sh(returnStdout: true, script: 'env').split('\n')
                    envs.each { variable  ->
                        if(variable.split('=')[0] in keys){
                            // println("find this --> " + variable.split('='))
                            println "Found --- $variable"
                        }
                    }
                }
//                 echo "${env.FIRST_VAR}"
//                 echo "${env.SECOND_VAR}"
            }
        }
    }
}

