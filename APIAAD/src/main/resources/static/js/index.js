var azureConfig = {
    instance: 'https://login.microsoftonline.com/',
    tenant: document.getElementById('val1').innerHTML,
    clientId: document.getElementById('val2').innerHTML,
    cacheLocation: 'localStorage', // enable this for IE, as sessionStorage does not work for localhost.
    endpoints: {
      graphApiUri: "https://graph.microsoft.com"
    }
};

var authContext = new AuthenticationContext(azureConfig);
  
var isCallback = authContext.isCallback(window.location.hash);
authContext.handleWindowCallback();

if (isCallback && !authContext.getLoginError()) {
    window.location = authContext._getItem(authContext.CONSTANTS.STORAGE.LOGIN_REQUEST);
}

function login() {
    authContext.login();
}

function logout() {
    authContext.logOut();
}

var info = document.getElementById('info');
var user = authContext.getCachedUser(); 

if (user) {
    console.log(user);
        info.innerText = 'username: ' + user.userName + ' name: ' + user.profile.name;
      } else {
        info.innerText = 'not logged in yet';
      }

      var result = document.getElementById('result');

           authContext.acquireToken(authContext.config.clientId, function(error, token) {
             if (error) {
               return;
             }
     
             var headers = new Headers({
               'Accept': 'application/json;charset=UTF-8',
               'Content-Type': 'application/json;charset=UTF-8',
               'Authorization': 'Bearer ' + token
             });
     
             var params = {
               method: 'GET',
               headers: headers
             };
     
             fetch('http://localhost:8080/token', params).then((response) => {
               return response.json();
             }).then((json) => {
               result.innerText = json.content;
             }).catch(function(err) {
             });

            var user = authContext.getCachedUser();

             if (error) {
               return;
             }


             let data_body = {
                 ipAddr:user.profile.ipaddr,
                 oid:user.profile.oid,
                 name:user.profile.name,
                 uniqueName:user.userName
               };


             fetch('http://localhost:8080/usuarios', {
               method: 'POST',
               body:JSON.stringify(data_body),
               headers: {
                 'Accept': 'application/json',
                 'Content-Type': 'application/json',
                 'Authorization': 'Bearer ' + token
               }})
               .then(response => response.json()) 
             }); 


