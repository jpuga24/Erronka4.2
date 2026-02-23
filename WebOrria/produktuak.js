const API_URL = "https://fakestoreapi.com/products";

let categoriasGlobal = []; // Guardar categorías
let productosGlobal = [];  // Guardar todos los productos

async function ProduktuakKargatu() {
    const contenedor = document.getElementById("produktuak");
    const contenedorCategorias = document.getElementById("categorias");

    try {
        const res = await fetch(API_URL);
        const productos = await res.json();
        productosGlobal = productos;

        // Sacar categorías únicas
        const categorias = [...new Set(productos.map(p => p.category))];
        categoriasGlobal = categorias;

        // Crear botones de categoría
        categorias.forEach((cat, i) => {
            const btn = document.createElement("button");
            btn.textContent = cat.toUpperCase();
            if(i===0) btn.classList.add("active"); // primera categoría activa
            btn.onclick = () => mostrarCategoria(cat, btn);
            contenedorCategorias.appendChild(btn);
        });

        // Mostrar la primera categoría por defecto
        mostrarCategoria(categorias[0], contenedorCategorias.querySelector("button"));

    } catch (e) {
        contenedor.innerHTML = "<p>Ezin dira produktuak kargatu</p>";
        console.error(e);
    }
}

// Mostrar productos de una categoría
function mostrarCategoria(categoria, botonActivo) {
    const contenedor = document.getElementById("produktuak");
    contenedor.innerHTML = "";

    // Quitar clase active de todos los botones
    document.querySelectorAll("#categorias button").forEach(b => b.classList.remove("active"));
    botonActivo.classList.add("active");

    // Filtrar productos de la categoría
    const productos = productosGlobal.filter(p => p.category === categoria);

    const grid = document.createElement("div");
    grid.className = "produktuak";

    productos.forEach(p => {
        const div = document.createElement("div");
        div.className = "produktua";

        div.innerHTML = `
            <img src="${p.image}" alt="${p.title}">
            <h4>${p.title}</h4>
            <p>${p.price.toFixed(2)}€</p>
            <button>EROSI</button>
        `;

        div.querySelector("button").onclick = () => {
            SaskiraGehitu(p.id, p.title, p.price);
        };

        grid.appendChild(div);
    });

    contenedor.appendChild(grid);
}

// Ejecutar al cargar
document.addEventListener("DOMContentLoaded", ProduktuakKargatu);
