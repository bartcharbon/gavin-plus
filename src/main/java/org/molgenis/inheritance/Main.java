package org.molgenis.inheritance;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.molgenis.calibratecadd.support.GavinUtils;
import org.molgenis.cgd.CGDEntry;
import org.molgenis.cgd.LoadCGD;
import org.molgenis.data.annotation.makervcf.structs.GavinRecord;
import org.molgenis.inheritance.model.Gene;
import org.molgenis.inheritance.model.InheritanceResult;
import org.molgenis.inheritance.model.Pedigree;
import org.molgenis.inheritance.ped.PedParser;
import org.molgenis.inheritance.tree.Start;
import org.molgenis.vcf.VcfReader;
import org.molgenis.vcf.VcfRecord;

public class Main {

  public static final String INPUT = "input";
  public static final String OUTPUT = "output";
  public static final String PED = "ped";
  public static final String CGD = "cgd";


  public static void main(String[] args) throws Exception {
    Start start = new Start();
    List<GavinRecord> records = new ArrayList<>();
    PedParser pedParser = new PedParser();
    OptionParser parser = createOptionParser();
    OptionSet options = parser.parse(args);

    File inputVcfFile = getFileFromOption(options, INPUT);
    File cgdFile = getFileFromOption(options, CGD);
    File pedFile = getFileFromOption(options, PED);

    VcfReader vcf = GavinUtils.getVcfReader(inputVcfFile);
    Map<String, CGDEntry> cgd = LoadCGD.loadCGD(cgdFile);

    Iterator<VcfRecord> variants = vcf.iterator();

    while (variants.hasNext()) {
      VcfRecord record = variants.next();
      GavinRecord gavinRecord = new GavinRecord(
          record);//TODO: if this will be a independent tool, than refactor to work of "VcfRecord"
      for (String geneName : gavinRecord.getGenes()) {
        CGDEntry cgdEntry = cgd.get(geneName);
        if (cgdEntry
            != null) {//FIXME: No hit in CGD -> What to do -> probably "inheritance mode = unknown?"
          Gene gene = Gene.create(geneName, cgdEntry);
          List<Pedigree> pedigrees = pedParser.parse(pedFile);
          for (Pedigree pedigree : pedigrees) {
            //FIXME: Per allele?
            InheritanceResult inheritance = start
                .filter(gavinRecord, records, gene, pedigree);
            System.out.println(
                pedigree.getChild().getSampleId() + " - " + gene.getGeneName() + " - "
                    + inheritance);
          }
        } else {
          System.out.println(String.format("No CGD for gene %s", geneName));
        }
      }
    }
  }

  private static File getFileFromOption(OptionSet options, String option)
      throws FileNotFoundException {
    if (!options.has(option)) {
      throw new RuntimeException(option);//FIXME: proper exception
    }
    File file = (File) options.valueOf(option);
    if (!file.exists()) {
      throw new FileNotFoundException(String.format("%s file not found at %s ", option, file));
    } else if (file.isDirectory()) {
      throw new RuntimeException(
          String.format("%s file location is a directory, not a file!", option));
    }
    return file;
  }

  private static OptionParser createOptionParser() {
    OptionParser parser = new OptionParser();

    parser.acceptsAll(asList("i", INPUT), "Input VCF file").withRequiredArg().ofType(File.class);
    parser.acceptsAll(asList("o", OUTPUT), "Output RVCF file").withRequiredArg()
        .ofType(File.class);
    parser.acceptsAll(asList("d", CGD), "CGD file").withRequiredArg().ofType(File.class);
    parser.acceptsAll(asList("p", PED), "Pedigree file").withRequiredArg().ofType(File.class);

    return parser;
  }
}
