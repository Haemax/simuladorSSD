import java.util.Scanner;

public class SimSSD {

    public static void main(String[] args) {
        // valida se tem argumentos suficientes, senao da erro
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

        // instanciando a estrategia direct mapped
        FTLStrategy ftl = new DirectMappedFTL(b, p, tr, tp, te);

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

            // output pedido: LPN OP LAT
            System.out.println(lpn + " " + op + " " + latencia);
        }

        // imprime o WA no final
        System.out.printf("# WA %.2f\n", ftl.getWriteAmplification());
        scanner.close();
    }
}

// implementacao do direct mapped 
class DirectMappedFTL{

    public DirectMappedFTL(int b, int p, int tr, int tp, int te) {
        super(b, p, tr, tp, te);
    }

    @Override
    public long read(int lpn) {
        return tr;
    }

    @Override
    public long write(int lpn) {
        totalLogicalWrites++;

        // logica do direct mapped eh ruim
        // 1. le o bloco inteiro (todos as paginas P)
        // 2. apaga o bloco (TE)
        // 3. reescreve o bloco inteiro com os dados novos (P * TP)
        
        long readTime = (long) p * tr;
        long eraseTime = te;
        long programTime = (long) p * tp;

        // contabiliza escritas fisicas (escreveu o bloco todo de volta)
        totalPhysicalWrites += p;

        return readTime + eraseTime + programTime;
    }
}