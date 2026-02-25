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
            if(i === 0) btn.classList.add("active");
            btn.onclick = () => kategoriakEnseinatu(cat, btn);
            KategoriaKontenedorea.appendChild(btn);
        });

        // Llamamos a la función inicial
        if (kategoriak.length > 0) {
            kategoriakEnseinatu(kategoriak[0], KategoriaKontenedorea.querySelector("button"));
        }

    } catch (e) {
        kontenedorea.innerHTML = "<p>Ezin dira produktuak kargatu</p>";
        console.error(e);
    }
}

function kategoriakEnseinatu(categoria, botonActivo) {
    const kontenedorea = document.getElementById("produktuak"); // Variable correcta
    kontenedorea.innerHTML = "";

    document.querySelectorAll("#kategoriak button").forEach(b => b.classList.remove("active"));
    if (botonActivo) botonActivo.classList.add("active");

    const produktuak = produktuakGlobal.filter(p => p.category === categoria);

    const grid = document.createElement("div");
    grid.className = "produktua-grid"; // Cambiado para evitar conflicto de CSS con el item

    produktuak.forEach(p => {
        const div = document.createElement("div");
        div.className = "produktua";

        div.innerHTML = `
            <img src="${p.image}" alt="${p.title}" style="width:100px;">
            <h4>${p.title}</h4>
            <p>${p.price.toFixed(2)}€</p>
            <button>EROSI</button>
        `;

        div.querySelector("button").onclick = () => {
            // Asegúrate de que esta función existe en tu código
            if (typeof SaskiraGehitu === "function") {
                SaskiraGehitu(p.id, p.title, p.price);
            } else {
                console.log("Gehituta:", p.title);
            }
        };

        grid.appendChild(div);
    });

    kontenedorea.appendChild(grid); // ¡Corregido! Ahora usa 'kontenedorea'
}

document.addEventListener("DOMContentLoaded", ProduktuakKargatu);