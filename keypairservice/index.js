const express = require('express');
const cors = require('cors');
const EC = require("elliptic").ec
const ec = new EC('secp256k1')

const app = express();

app.use(cors());
app.use(express.json());

app.get('/keypair', (req, res) => {
    const keyPair = ec.genKeyPair();
    const privateKey = keyPair.getPrivate('hex')
    const publicKeyUncompressed = keyPair.getPublic(false, 'hex')
    const publicKeyCompressed = keyPair.getPublic(true, 'hex')
    res.json({
        privateKey,
        publicKeyUncompressed,
        publicKeyCompressed
    });
});

app.post("/publickeycheck", (req, res) => {
    const {publicKey} = req.body;
    
    try {
        ec.keyFromPublic(publicKey, 'hex');
        res.json({
            valid: true
        });
    } catch (error) {
        return res.json({
            valid: false
        });
    }
});
    

app.post('/getpublickey', (req, res) => {
    const { privateKey } = req.body;
    const keyPair = ec.keyFromPrivate(privateKey);
    const publicKeyUncompressed = keyPair.getPublic(false, 'hex')
    const publicKeyCompressed = keyPair.getPublic(true, 'hex')
    res.json({
        privateKey,
        publicKeyUncompressed,
        publicKeyCompressed
    });
});

app.post('/verify', (req, res)=> {
    const { publicKey, message, signature } = req.body;
    
    const keyPair = ec.keyFromPublic(publicKey, 'hex');
    const isValid = keyPair.verify(message, signature);
    res.json({
        isValid
    });
})

app.post('/sign', (req, res) => {
    const { privateKey, message } = req.body;
    const keyPair = ec.keyFromPrivate(privateKey);
    const publicKey = keyPair.getPublic(true, 'hex');
    if (!message.startsWith(publicKey)) {
        return res.json({
            error: "Message does not contains public key"
        }).status(400);
    }
    const signature = keyPair.sign(message).toDER('hex');
    res.json({
        signature
    });
})

app.post("/hash", async(req, res) => {
    const hashHex = await calcHashHex(req.body);
    res.json({
        hashHex
    });
})

app.post("/mine", async(req, res) => {
    var c = 0;
    var hashHex = await calcHashHex(req.body);
    while(!hashHex.startsWith('000')) {
        c++;
        req.body.nonce = c;
        hashHex = await calcHashHex(req.body);
    }
    return res.json({
        hashHex,
        nonce: c
    });
})

app.post('/login', (req, res) => {
    const {publicKey, privateKey} = req.body;
    try {
    const keyPair = ec.keyFromPrivate(privateKey);
    if (publicKey!==keyPair.getPublic(true, 'hex')) {
        res.status(401).json({
            error: "Invalid key"
        })
        return;
    }
    return res.json({
        success: "Logged in"
    })
    } catch (error) {
        res.status(401).json({
            error: "Invalid key"
        })
        return;
    }
})

app.listen(5000, () => {
    console.log('Server is running on port 5000');
});

async function calcHashHex(obj) {
    const encoder = new TextEncoder();
    const objString = JSON.stringify(obj);
    // console.log(objString);
    const data = encoder.encode(objString);
    const hashBuffer = await crypto.subtle.digest('SHA-256', data);
    const hashArray = Array.from(new Uint8Array(hashBuffer));
    const hashHex = hashArray.map(byte => byte.toString(16).padStart(2, '0')).join('');
    return hashHex;
}