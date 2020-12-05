module.exports = {
    proxy: {
        "/api": {
            target: "http://localhost:8060/",
            ws: true
        }
    },
    plugins: [
        "@snowpack/plugin-typescript",
    ]
};
