const proxy = require("http2-proxy");

const localhost = (req, res) =>
  proxy.web(req, res, {
    hostname: "localhost",
    port: 8060,
  });

module.exports = {
  routes: [
    {
      match: "all",
      src: "/api/.*",
      dest: localhost,
      upgrade: (req, socket, head) => {
        const defaultWSHandler = (err, req, socket, head) => {
          if (err) {
            console.error("proxy error", err);
            socket.destroy();
          }
        };

        proxy.ws(
          req,
          socket,
          head,
          {
            hostname: "localhost",
            port: 8060,
          },
          defaultWSHandler
        );
      },
    },
    {
      match: "all",
      src: "/data/.*",
      dest: localhost,
    },
    {
      match: "all",
      src: "/rest/.*",
      dest: localhost,
    },
  ],
  mount: {
    src: "/",
  },
  plugins: [["@snowpack/plugin-typescript"]],
};
