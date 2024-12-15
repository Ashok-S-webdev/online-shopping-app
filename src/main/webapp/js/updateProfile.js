window.onload = function() {
    const form = document.getElementById('update-profile-form')

    fetch(`/online-shopping-app/api/profile/getUserProfile`)
        .then(response => response.json())
        .then(user => {
            document.getElementById('userId').value = user.userId
            document.getElementById('username').value = user.username
            document.getElementById('email').value = user.email
            document.getElementById('mobile').value = user.mobile

            const imageUrl = `data:image/jpeg;base64,${user.image}`;
            const profileImage = document.getElementById('profile-picture').src = imageUrl;

            form.addEventListener('submit', function(event) {
                event.preventDefault()

                const formData = new FormData(this)
                console.log(formData)
                fetch('/online-shopping-app/api/profile/update', {
                    method: 'POST',
                    body: formData
                })
                .then(response => response.json())
                .then(result => {
                    if (result.status === 'success') {
                        alert(result.message)
                        window.location.href = 'profile.html'
                    } else {
                        alert(result.message)
                    }
                }).catch(error => {
                    console.log('Error updating profile', error)
                })
            })
        }).catch(error => {
            console.log(error)
        })
    
}