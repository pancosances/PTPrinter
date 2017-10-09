package eu.panco.ptprinter.receipt;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

import eu.panco.ptprinter.PrinterCommand;

public class ReceiptDocument {

    private final String originalReceiptString;
    private String count;
    private String id;
    private String dkp;
    private String date;
    private String sum;
    private String repository;
    private Company company;
    private ArrayList<Item> items;


    private ArrayList<byte[]> bytes;

    public ReceiptDocument(String receiptString) {
        this.originalReceiptString = receiptString;
        this.company = new Company();
        this.items = new ArrayList<Item>();
        this.bytes = new ArrayList<byte[]>();
        parse(receiptString);

        this.bytes.add(PrinterCommand.reset);
        this.bytes.add(PrinterCommand.set_tab);
        this.bytes.add(PrinterCommand.set_charset_cp852);
        this.bytes.add(PrinterCommand.align_center);
        this.bytes.add(PrinterCommand.bold_on);
        this.bytes.add(PrinterCommand.spacing_double);
        this.bytes.add(b(company.getName()));
        this.bytes.add(PrinterCommand.spacing_default);
        this.bytes.add(PrinterCommand.bold_off);
        this.bytes.add(PrinterCommand.new_line);
        this.bytes.add(b(company.getAddress()));
        this.bytes.add(PrinterCommand.new_line);
        this.bytes.add(b(this.repository));
        this.bytes.add(PrinterCommand.new_line);
        this.bytes.add(PrinterCommand.new_line);
        this.bytes.add(b("I¬O: "));
        this.bytes.add(b(company.getIco()));
        this.bytes.add(PrinterCommand.new_line);
        this.bytes.add(b("DI¬: "));
        this.bytes.add(b(company.getDic()));
        this.bytes.add(PrinterCommand.new_line);
        this.bytes.add(b("--------------------------------"));
        this.bytes.add(PrinterCommand.new_line);
        this.bytes.add(PrinterCommand.align_left);
        this.bytes.add(b("#n zov"));
        this.bytes.add(PrinterCommand.tab);
        this.bytes.add(b("ks"));
        this.bytes.add(PrinterCommand.tab);
        this.bytes.add(b("cena"));
        this.bytes.add(PrinterCommand.tab);
        this.bytes.add(b(" celkom#"));
        this.bytes.add(PrinterCommand.new_line);
        this.bytes.add(b("--------------------------------"));
        for (Item item : this.items) {
            this.bytes.add(PrinterCommand.new_line);
            this.bytes.add(b(item.getName()));
            this.bytes.add(PrinterCommand.new_line);
            this.bytes.add(PrinterCommand.tab);
            this.bytes.add(b(item.getCount()));
            this.bytes.add(PrinterCommand.tab);
            this.bytes.add(b(item.getPrice()));
            this.bytes.add(PrinterCommand.tab);
            this.bytes.add(b(String.format("%8s", item.getSumPrice())));
        }
        this.bytes.add(PrinterCommand.new_line);
        this.bytes.add(b("********************************"));
        this.bytes.add(PrinterCommand.new_line);
        this.bytes.add(b("Celkom:"));
        this.bytes.add(PrinterCommand.tab);
        this.bytes.add(PrinterCommand.tab);
        this.bytes.add(b(String.format("%12s EUR", this.sum)));
        this.bytes.add(PrinterCommand.new_line);
        this.bytes.add(PrinterCommand.new_line);
        this.bytes.add(b(this.id));
        this.bytes.add(PrinterCommand.new_line);
        this.bytes.add(b(this.date));
        this.bytes.add(PrinterCommand.tab);
        this.bytes.add(PrinterCommand.tab);
        this.bytes.add(b(String.format("%8s", this.count)));
        this.bytes.add(PrinterCommand.new_line);
        this.bytes.add(PrinterCommand.align_center);
        this.bytes.add(b("DKP: "));
        this.bytes.add(b(this.dkp));

        this.bytes.add(PrinterCommand.new_line);
        this.bytes.add(PrinterCommand.new_line);
        this.bytes.add(PrinterCommand.new_line);
    }

    private byte[] b(String text) {
        try {
            return text.getBytes("Cp852");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return text.getBytes();
    }

    private void parse(String receiptString) {
        String[] parsed = receiptString.split("\n");
        for (int i = 0; i < parsed.length; i++) {
            String row = parsed[i];
            switch (i) {
                // Receipt counter
                case 0:
                    this.count = row.split(" ")[3]; // POKLADNI¬Ní DOKLAD ¬. 0002
                    break;
                // Company name
                case 1:
                    company.setName(row); // PANCO & Brothers s.r.o.
                    break;
                // Company address
                case 2:
                    company.setAddress(row); // Ort çe 81, 04444 Plosk‚
                    break;
                case 3: // Predajn‚ miesto:
                case 4: // Prenosn  pokladnica
                    this.repository = row;
                    break;
                // Company DIC
                case 5:
                    company.setDic(row.split(" ")[1]); // DI¬: 2120167951
                    break;
                // DKP
                case 6:
                    this.dkp = row.split(" ")[1]; // DKP: 99921201679510001
                    break;
                // Receipt Date
                case 7:
                    this.date = row; // 18.5.2016 22:40
                    break;
                // Receipt ID
                case 8:
                    this.id = row.split(" ")[2]; // ID k¢d: 02D1262FACDF434391262FACDF7343F2
                    break;
                default:
                    // SUM
                    if (row.startsWith("SPOLU")) { // SPOLU: 3,00ÿ?
                        String sum = row.split(":")[1].trim();
                        this.sum = sum.substring(0, sum.length()-2);
                    } else {
                        Item item = new Item();
                        item.setName(parsed[i]);
                        String[] itemString = parsed[++i].split(" ");
                        item.setCount(itemString[0].trim());
                        item.setPrice(itemString[1].substring(0, itemString[1].length()-4));
                        item.setSumPrice(itemString[2].substring(0, itemString[2].length()-2));
                        this.items.add(item);
                    }
                    break;
            }
        }
    }

    public ArrayList<byte[]> getListBytes() {
        return bytes;
    }
}
