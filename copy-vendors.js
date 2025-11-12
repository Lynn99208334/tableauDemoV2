// copy-vendors.js
const fs = require('fs');
const path = require('path');

const files = [
    // JS
    ['node_modules/sweetalert2/dist/sweetalert2.all.min.js',
        'src/main/resources/static/js/vendor/sweetalert2.all.v11.10.6.min.js'],
    ['node_modules/sockjs-client/dist/sockjs.min.js',
        'src/main/resources/static/js/vendor/sockjs.v1.6.1.min.js'],
    ['node_modules/@stomp/stompjs/bundles/stomp.umd.min.js',
        'src/main/resources/static/js/vendor/stomp.umd.v7.0.0.min.js'],
    // CSS（建議一併提供）
    ['node_modules/sweetalert2/dist/sweetalert2.min.css',
        'src/main/resources/static/css/vendor/sweetalert2.v11.10.6.min.css']
];

for (const [src, dst] of files) {
    const absSrc = path.resolve(src);
    const absDst = path.resolve(dst);
    fs.mkdirSync(path.dirname(absDst), { recursive: true });
    fs.copyFileSync(absSrc, absDst);
    console.log(`copied: ${src} -> ${dst}`);
}
