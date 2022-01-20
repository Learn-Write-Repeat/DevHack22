const routes = [
  {
    path: "/",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      {
        path: "",
        component: () => import("src/pages/Chat.vue"),
        name: "chat",
        meta: {
          showMenu: true,
          statusBarStyle: { color: "#5455e5" },
        },
      },
      {
        path: "signup",
        component: () => import("src/pages/Signup.vue"),
        name: "Login/Signup",
        meta: {
          showMenu: false,
          statusBarStyle: { color: "#5455e5" },
        },
      },
      {
        path: "chat",
        component: () => import("src/pages/Chat.vue"),
        name: "chat",
        meta: {
          showMenu: true,
          statusBarStyle: { color: "#5455e5" },
        },
      },
      {
        path: "profile",
        component: () => import("pages/Profile.vue"),
        name: "profile",
        meta: {
          showMenu: true,
          statusBarStyle: { color: "#5455e5" },
        },
      },
      {
        path: "profile-viewer",
        component: () => import("pages/ProfileViewer.vue"),
        name: "profile-viewer",
        meta: {
          showMenu: true,
          statusBarStyle: { color: "#5455e5" },
        },
      },
      {
        path: "notes",
        component: () => import("pages/Notes.vue"),
        name: "notes",
        meta: {
          showMenu: true,
          statusBarStyle: { color: "#5455e5" },
        },
      },
    ],
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: "/:catchAll(.*)*",
    component: () => import("pages/Error404.vue"),
  },
];

export default routes;
