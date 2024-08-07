storage "file" {
  path = "/Users/alexking/Documents/GitHub/AeroBankApp/AeroBankApp/src/main/resources/config.hcl"
}

listener "tcp" {
  address     = "127.0.0.1:8200"
  tls_disable = "true"
}