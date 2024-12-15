
async function loadUsers() {
    try {
        const response = await fetch('/online-shopping-app/api/admin/getUsers', {
            method: 'GET'
        })

        const users = await response.json()

        const userList = document.getElementById('user-list');
        users.forEach(user => {
            console.log(user)
            const userDiv = document.createElement('div');
            const imageUrl = `data:image/jpeg;base64,${user.image}`
            userDiv.classList.add('user')
            userDiv.innerHTML = `
            <img src="${imageUrl}" alt="${user.image}" />
            <h3>${user.username}</h3>
            <p>${user.email}</p>
            <p>${user.mobile}</p>
            `
            userList.appendChild(userDiv);
        })
    } catch (e) {
        console.error("An error occured: ", e)
    }
}


document.addEventListener('DOMContentLoaded', function () {
    loadUsers();
})