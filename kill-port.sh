#!/bin/bash
# 杀掉占用 18010 端口的进程
PID=$(lsof -ti :18010)
if [ -n "$PID" ]; then
  kill -9 $PID
  echo "已杀掉端口 18010 的进程: $PID"
else
  echo "端口 18010 无进程占用"
fi
