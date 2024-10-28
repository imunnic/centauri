import { defineStore } from 'pinia';

export const useImpresionStore = defineStore('impresionStore', {
  actions: {
    imprimir(nodoId) {
      const nodo = document.getElementById(nodoId)?.innerHTML;
      if (!nodo) {
        console.warn(`No se encontró el elemento con ID: ${nodoId}`);
        return;
      }

      const ventanaImpresion = window.open("", "_blank");
      ventanaImpresion.document.open();
      ventanaImpresion.document.write(`
        <html>
          <head>
            <title>Imprimir</title>
            ${Array.from(document.head.getElementsByTagName("link"))
              .map(link => link.outerHTML)
              .join("")}
            ${Array.from(document.head.getElementsByTagName("style"))
              .map(style => style.outerHTML)
              .join("")}
            <style>
              @media print {
                body {
                  zoom: 80%;
                }

                * {
                  page-break-inside: avoid;
                }
              
                button, .no-print {
                  display: none;
                }
              }
            </style>
          </head>
          <body onload="window.print(); window.close();">
            ${nodo}
          </body>
        </html>
      `);
      ventanaImpresion.document.close();
    }
  }
});
