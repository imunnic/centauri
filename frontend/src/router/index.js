import { createRouter, createWebHashHistory } from "vue-router";
import { useUsuariosStore } from "@/store/usuariosStore.js";
const routes = [
  {
    path: "/",
    redirect: "/fichas",
  },
  {
    path: "/planificacion",
    name: "planificacion",
    component: () => import("../vistas/PlanificacionVista.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/usuario",
    name: "usuario",
    component: () => import("../vistas/UsuarioVista.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/equipamientos",
    name: "equipamientos",
    component: () => import("../vistas/EquipamientoVista.vue"),
  },
  {
    path: "/fichas",
    name: "fichas",
    component: () => import("../vistas/FichasVista.vue"),
  },
  {
    path: "/fichas/:id",
    name: "fichaDetalle",
    component: () => import("../vistas/FichaDetalleVista.vue"),
  },
  {
    path: "/fichas/crear",
    name: "crearFicha",
    component: () => import("../vistas/CrearFichaVista.vue"),
    meta: { requiresAuth: true, requiresRole: ['DIPLOMADO', 'ECEF'] },
  },
  {
    path: "/fichas/editar/:id",
    name: "editarFicha",
    component: () => import("../vistas/EditarFichaVista.vue"),
    meta: { requiresAuth: true, requiresRole: ['DIPLOMADO', 'ECEF'] },
  },
  {
    path: "/gestion",
    name: "gestionUsuarios",
    component: () => import("../vistas/GestionUsuariosVista.vue"),
    meta: { requiresAuth: true, requiresRole: ['ECEF'] },
  },
  {
    path: "/ejercicios",
    name: "ejercicios",
    component: () => import("../vistas/EjerciciosVista.vue"),
  },
  {
    path: "/sesiones/:id",
    name: "sesionDetalle",
    component: () => import("../vistas/SesionDetalleVista.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/grupos",
    name: "grupos",
    component: () => import("../vistas/GruposVista.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/contador/:id",
    name: "contador",
    component: () => import("../vistas/ContadorVista.vue")
  },
  {
    path: "/registro/:id",
    name: "registro",
    component: () => import("../vistas/RegistroVista.vue")
  },
  {
    path: '/:catchAll(.*)',
    redirect: '/usuario'
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const usuariosStore = useUsuariosStore();

  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!usuariosStore.isLogged) {
      return next({ name: 'fichas' });
    }

    if (to.meta.requiresRole && !to.meta.requiresRole.includes(usuariosStore.perfil)) {
      return next({ name: 'usuario' });
    }
  }

  next();
});

export default router;
