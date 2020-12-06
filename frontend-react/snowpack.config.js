module.exports = {
    proxy: {
        "/api": {
            target: "http://localhost:8060/",
            ws: true
        },
        "/data": {
            target: "http://localhost:8060/",
            ws: false
        }
    },
    plugins: [
        "@snowpack/plugin-typescript",
    ]
};
