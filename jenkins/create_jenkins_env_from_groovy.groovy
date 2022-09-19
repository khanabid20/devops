import hudson.model.*

def data= build.buildVariableResolver.resolve("MY_BUILD_PARAMETERS")
println(data)

def build = Thread.currentThread().executable
def array_of_params = []

if (data){
    data.split(';').each { entry ->
        if(!(entry =~ /.\w*=\w*/)){
            println("Invalid data format '" + entry + "'.\nShould be of format key1=val1;key2=val2.")
            return true
        }
        array_of_params.add(new StringParameterValue(entry.split('=')[0], entry.split('=')[1]))
    }
    def pa = new ParametersAction(array_of_params)
    build.addAction(pa)
} else {
  println('Manual run are not allowed.')
  return false
}

'''
ref:
- https://stackoverflow.com/a/21936515/9454903
'''
