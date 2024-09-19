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
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
