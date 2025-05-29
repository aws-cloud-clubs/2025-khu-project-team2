variable "region" {
  description = "AWS Region"
  type        = string
  default     = "ap-northeast-2"
}

variable "cluster_name" {
  description = "MSK Cluster Name"
  type        = string
  default     = "my-msk-cluster"
}
