function fn() {

    let port = karate.properties['spring.port']
    let mockPort = karate.properties['mockServerPort']

    karate.configure('connectTimeout', 15000);
    karate.configure('readTimeout', 30000);

    let config = {
        mockServerUrl: `http://localhost:${mockPort}`
    };

    karate.configure('url', `http://localhost:${port}` )
    return config
}