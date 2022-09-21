import org.acegisecurity.*
import hudson.model.User
import hudson.tasks.Mailer
import jenkins.security.*
import java.util.Date

// todo - yet to write code for checking last logged in users

User.getAll().each{ u ->
  def prop = u.getProperty(LastGrantedAuthoritiesProperty)
  def mailAddress = u.getProperty(Mailer.UserProperty.class).getAddress()
  def realUser = false
  def timestamp = null
  if (prop) {
    realUser=true
    timestamp = new Date(prop.timestamp).toString()
  }

  if (realUser){
    println u.getId() + ':' + mailAddress + ':' + u.getDisplayName() + ':Jenkins-User:' + u?.getDescription() + ':timestamp=' + timestamp
  } else if (realUser==false){
    println u.getId() + ':' + u.getDisplayName() + ':No-Jenkins-User:' + u?.getDescription()
  }
}
return

'''
ref:
- https://stackoverflow.com/a/30955925/9454903
- https://github.com/cloudbees/jenkins-scripts/blob/master/get-lastlogin-users.groovy
'''
