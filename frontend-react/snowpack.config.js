module.exports = {
    proxy: {
        "/api": "http://localhost:8060/api"
    },
    plugins: [
        "@snowpack/plugin-typescript",
    ]
};
