const target = "http://localhost:8060/";
module.exports = {
  proxy: {
    "/api": {
      target,
      ws: true,
    },
    "/data": {
      target,
      ws: false,
    },
    "/rest": {
      target,
      ws: false,
    },
  },
  mount: {
    src: "/",
  },
  plugins: ["@snowpack/plugin-typescript"],
};
