const API_URL = "https://fakestoreapi.com/products";

let kategoriakGlobal = [];
let produktuakGlobal = [];

async function ProduktuakKargatu() {
    const kontenedorea = document.getElementById("produktuak");
    const KategoriaKontenedorea = document.getElementById("kategoriak");

    try {
        const res = await fetch(API_URL);
        const produktuak = await res.json();
        produktuakGlobal = produktuak;

        const kategoriak = [...new Set(produktuak.map(p => p.category))];
        kategoriakGlobal = kategoriak;

        kategoriak.forEach((cat, i) => {
            const btn = document.createElement("button");
            btn.textContent = cat.toUpperCase();
            if(i===0) btn.classList.add("active");
            btn.onclick = () => mostrarCategoria(cat, btn);
            KategoriaKontenedorea.appendChild(btn);
        });

        kategoriakEnseinatu(kategoriak[0], KategoriaKontenedorea.querySelector("button"));

    } catch (e) {
        kontenedorea.innerHTML = "<p>Ezin dira produktuak kargatu</p>";
        console.error(e);
    }
}

function kategoriakEnseinatu(categoria, botonActivo) {
    const kontenedorea = document.getElementById("produktuak");
    kontenedorea.innerHTML = "";

    document.querySelectorAll("#categorias button").forEach(b => b.classList.remove("active"));
    botonActivo.classList.add("active");

    const produktuak = produktuakGlobal.filter(p => p.category === categoria);

    const grid = document.createElement("div");
    grid.className = "produktuak";

    produktuak.forEach(p => {
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

document.addEventListener("DOMContentLoaded", ProduktuakKargatu);
