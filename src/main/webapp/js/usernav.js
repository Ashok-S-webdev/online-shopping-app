window.onload = function() {
    const navbar = document.getElementById("navbar");
    const currentPage = window.location.pathname.split('/').pop()
    navbar.innerHTML = `
    <nav class="navbar">
            <div class="navbar-logo">
                <a href="user.html">Online Shopping</a>
            </div>
            <ul class="navbar-links">
                <li><a href="user.html" id="home-link">Shop</a></li>
                <li><a href="cart.html" id="cart-link">Cart</a></li>
                <li><a href="profile.html" id="profile-link">Profile</a></li>
                <li><a id="logout-link" href="#">Logout</a></li>
            </ul>
        </nav>
    `
    
    if (currentPage === 'user.html') {
        document.getElementById('home-link').classList.add('active')
    } else if (currentPage === 'cart.html') {
        document.getElementById('cart-link').classList.add('active')
    } else if (currentPage === 'profile.html') {
        document.getElementById('profile-link').classList.add('active')
    }


    document.getElementById('logout-link').addEventListener('click', async function() {
        try {
            const response = await fetch('/online-shopping-app/api/logout', {
                method: 'POST',
            });
    
            if (response.redirected) {
                window.location.href = response.url;
            }
        } catch (error) {
            console.error('Error during logout:', error);
            alert('An error occurred during logout.');
        }
    })

    document.querySelectorAll('.navbar-links a').forEach(link => {
        link.addEventListener('click', function(e) {
            if (link.href.split('/').pop() === currentPage) {
                e.preventDefault(); 
            }
        });
    });
}

