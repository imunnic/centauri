import { createRouter, createWebHistory } from "vue-router";

const routes = [
  {
    path: "/",
    redirect: "/fichas",
  },
  {
    path: "/fichas",
    name: "fichas",
    component: () => import("../vistas/FichasVista.vue"),
  },
  {
    path: "/fichas/:id",
    name: "fichaDetalle",
    component: () => import("../vistas/FichaDetalleVista.vue")
  },
  {
    path: "/fichas/crear",
    name: "crearFicha",
    component: () => import("../vistas/CrearFichaVista.vue")
  },
  {
    path: "/fichas/editar/:id",
    name: "editarFicha",
    component: () => import("../vistas/EditarFichaVista.vue")
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
