window.onload = function() {
    const navbar = document.getElementById("navbar");
    const currentPage = window.location.pathname.split('/').pop()
    navbar.innerHTML = `
    <nav class="navbar">
            <div class="navbar-logo">
                <a href="admin.html">Online Shopping</a>
            </div>
            <ul class="navbar-links">
                <li><a href="admin.html" id="users-page-link">Users</a></li>
                <li><a href="manage-products.html" id="products-page-link">Products</a></li>
                <li><a id="logout-link" href="#">Logout</a></li>
            </ul>
        </nav>
    `
    
    if (currentPage === 'admin.html') {
        document.getElementById('users-page-link').classList.add('active')
    } else if (currentPage === 'manage-products.html') {
        document.getElementById('products-page-link').classList.add('active')
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

