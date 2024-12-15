window.onload = function() {
    fetch('/online-shopping-app/api/hello')
    .then(response => response.text())
    .then(message => {
        document.getElementById('message').innerHTML = message
    })
    .catch(error => {
        document.getElementById('message').innerHTML = 'Error loading message'
        console.log(error)
    })
}