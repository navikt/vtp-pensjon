module.exports = {
    proxy: {
        "/api": {
            target: "http://localhost:8061/",
            ws: true
        }
    },
    plugins: [
        "@snowpack/plugin-typescript",
    ]
};
