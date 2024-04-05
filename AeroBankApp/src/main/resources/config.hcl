tee config.hcl
storage "file" {
  path = "vault-data"
}

listener "tcp" {
  tls_disable = "true"
}

path "sys/mounts/*" {
  capabilities = ["create", "read", "update", "delete", "list"]
}
