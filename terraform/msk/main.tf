provider "aws" {
  region = var.region
  profile = "default"
}

data "aws_availability_zones" "azs" {
  state = "available"
}

resource "aws_vpc" "msk_vpc" {
  cidr_block = "10.0.0.0/16"
  tags = {
    Name = "${var.cluster_name}-vpc"
  }
}

resource "aws_subnet" "msk_subnet" {
  count             = 2
  vpc_id            = aws_vpc.msk_vpc.id
  cidr_block        = cidrsubnet(aws_vpc.msk_vpc.cidr_block, 8, count.index)
  availability_zone = data.aws_availability_zones.azs.names[count.index]
  tags = {
    Name = "${var.cluster_name}-subnet-${count.index+1}"
  }
}

resource "aws_security_group" "msk_sg" {
  name        = "${var.cluster_name}-sg"
  description = "Allow Kafka traffic"
  vpc_id      = aws_vpc.msk_vpc.id

  ingress {
    from_port   = 9092
    to_port     = 9092
    protocol    = "tcp"
    cidr_blocks = [aws_vpc.msk_vpc.cidr_block]
  }

  ingress {
    from_port   = 9094
    to_port     = 9094
    protocol    = "tcp"
    cidr_blocks = [aws_vpc.msk_vpc.cidr_block]
  }

  ingress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    self        = true
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "${var.cluster_name}-sg"
  }
}

resource "aws_msk_cluster" "msk" {
  cluster_name           = var.cluster_name
  kafka_version          = "3.5.1"
  number_of_broker_nodes = 2

  broker_node_group_info {
    instance_type   = "kafka.t3.small"
    client_subnets  = aws_subnet.msk_subnet[*].id
    security_groups = [aws_security_group.msk_sg.id]
  }
  tags = {
    Name = var.cluster_name
  }
}
