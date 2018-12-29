set outdir=..\out\artifacts\Encrypt_jar
java -jar %outdir%\Encrypt.jar -xml enc_config.xml -src %outdir%\Encrypt.jar -dst %outdir%\encClass.jar -decxml %outdir%\dec_config.xml
