document.addEventListener('DOMContentLoaded', function() {
    loadUserDetails();
})

function loadUserDetails() {
    fetch('/online-shopping-app/api/profile/getUserProfile')
    .then(response => response.json())
    .then(user => {
        const imageUrl = `data:image/jpeg;base64,${user.image}`
        document.getElementById('profile-picture').src = imageUrl
        document.getElementById('user-name').innerHTML = user.username
        document.getElementById('user-email').innerHTML = user.email
        document.getElementById('user-mobile').innerHTML = user.mobile
    })
}