import java.util.Scanner;

public class SimSSD {

    public static void main(String[] args) {
        if (args.length < 6) {
            System.out.println("uso: java SimSSD <B> <P> <O> <TR> <TP> <TE>");
            return;
        }

        // lendo a configuracao
        int b = Integer.parseInt(args[0]);
        int p = Integer.parseInt(args[1]);
        double o = Double.parseDouble(args[2]); // overprovisioning nao usa no direct mas ta aqui
        int tr = Integer.parseInt(args[3]);
        int tp = Integer.parseInt(args[4]);
        int te = Integer.parseInt(args[5]);

        // instanciando a estrategia 
        DirectMappedFTL ftl = new DirectMappedFTL(b, p, tr, tp, te);

        Scanner scanner = new Scanner(System.in);

        // loop principal 
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split("\\s+");
            int lpn = Integer.parseInt(parts[0]);
            String op = parts[1];

            long latencia = 0;

            // processa a operacao
            if (op.equalsIgnoreCase("R")) {
                latencia = ftl.read(lpn);
            } else if (op.equalsIgnoreCase("W")) {
                latencia = ftl.write(lpn);
            }

            // output pedido
            System.out.println(lpn + " " + op + " " + latencia);
        }

        // imprime o WA no final
        System.out.printf("# WA %.2f\n", ftl.getWriteAmplification());
        scanner.close();
    }
}

// implementacao do direct mapped sem classe abstrata
class DirectMappedFTL {
    private int b, p, tr, tp, te;
    private long totalLogicalWrites = 0;
    private long totalPhysicalWrites = 0;

    public DirectMappedFTL(int b, int p, int tr, int tp, int te) {
        this.b = b;
        this.p = p;
        this.tr = tr;
        this.tp = tp;
        this.te = te;
    }

    public long read(int lpn) {
        return tr;
    }

    public long write(int lpn) {
        totalLogicalWrites++;

        // 1. le o bloco inteiro 
        // 2. apaga o bloco (TE) 
        // 3. reescreve o bloco inteiro 
        
        long readTime = (long) p * tr;
        long eraseTime = te;
        long programTime = (long) p * tp;

        // no mapeamento direto a escrita causa mt amplificacao 
        totalPhysicalWrites += p;

        return readTime + eraseTime + programTime;
    }

    public double getWriteAmplification() {
        return totalLogicalWrites == 0 ? 0 : (double) totalPhysicalWrites / totalLogicalWrites;
    }
}
