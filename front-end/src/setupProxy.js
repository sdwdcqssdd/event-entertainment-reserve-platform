const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
    app.use(
        '/api',
        createProxyMiddleware({
            target: 'http://123.56.102.167:8080',
            changeOrigin: true,
        })
    );
};
