import socket

HOST = "0.0.0.0"
PORT = 5000

def start_server():
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

    server.bind((HOST, PORT))
    server.listen(5)

    print(f"[INFO] Listening on {HOST}:{PORT} ...")

    while True:
        conn, addr = server.accept()
        print(f"[INFO] Connected by {addr}")

        handle_client(conn)


def handle_client(conn):
    buffer = ""
    handshake_done = False

    try:
        while True:
            data = conn.recv(1024)

            if not data:
                print("[INFO] Connection closed")
                break

            buffer += data.decode()

            while "\n" in buffer:
                line, buffer = buffer.split("\n", 1)
                packet = line.strip()

                if not handshake_done:
                    if packet == "#HELLO":
                        print("[HANDSHAKE] HELLO received")
                        conn.sendall(b"#READY\n")
                        handshake_done = True
                        continue
                    else:
                        print("[WARN] Expected HELLO, got:", packet)
                        continue

                handle_packet(packet)

    except Exception as e:
        print("[ERROR]", e)

    finally:
        conn.close()


def handle_packet(packet: str):
    if not packet.startswith("#IMU"):
        print("[WARN] Unknown packet:", packet)
        return

    try:
        parts = packet.split(",")

        gx = float(parts[1])
        gy = float(parts[2])
        gz = float(parts[3])

        ax = float(parts[4])
        ay = float(parts[5])
        az = float(parts[6])

        qx = float(parts[7])
        qy = float(parts[8])
        qz = float(parts[9])
        qw = float(parts[10])

        print(f"[IMU]")
        print(f"Gyro : {gx:.2f}, {gy:.2f}, {gz:.2f}")
        print(f"Acc  : {ax:.2f}, {ay:.2f}, {az:.2f}")
        print(f"Quat : {qx:.2f}, {qy:.2f}, {qz:.2f}, {qw:.2f}")
        print("-" * 40)

    except Exception as e:
        print("[ERROR] Failed to parse:", packet, e)


if __name__ == "__main__":
    start_server()