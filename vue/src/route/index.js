import { createRouter, createWebHashHistory } from "vue-router";
import Login from "@/components/Login.vue";
import Index from "@/components/Index.vue";

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: "/",
      redirect: "/login",
    },
    {
      path: "/login",
      name: "login",
      component: Login,
    },
    {
      path: "/index",
      name: "index",
      component: Index,
      meta: {
        requiresAuth: true,
      },
    },
  ],
});

router.beforeEach((to, from, next) => {
  const loginInfo = localStorage.getItem("userToken");

  if (to.meta.requiresAuth && !loginInfo) {
    next("/login");
    return;
  }

  if (to.path === "/login" && loginInfo) {
    next("/index");
    return;
  }

  next();
});

export default router;
