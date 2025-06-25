################################################################################
# outputs.tf
################################################################################

output "msk_cluster_arn" {
  description = "Amazon Resource Name (ARN) of the MSK cluster"
  value       = aws_msk_cluster.msk.arn
}

output "msk_bootstrap_brokers_plaintext" {
  description = "Comma-separated list of PLAINTEXT bootstrap brokers"
  value       = aws_msk_cluster.msk.bootstrap_brokers
}

output "msk_bootstrap_brokers_tls" {
  description = "Comma-separated list of TLS bootstrap brokers"
  value       = aws_msk_cluster.msk.bootstrap_brokers_tls
}

