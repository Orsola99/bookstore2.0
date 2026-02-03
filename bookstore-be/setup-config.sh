#!/bin/bash
# Script per inizializzare la configurazione dopo il clone

CONFIG_TEMPLATE="src/main/resources/config.properties.template"
CONFIG_FILE="src/main/resources/config.properties"

if [ ! -f "$CONFIG_FILE" ]; then
  cp "$CONFIG_TEMPLATE" "$CONFIG_FILE"
  echo "File di configurazione creato: $CONFIG_FILE"
  echo "** MODIFICALO CON I TUOI DATI LOCALI **"
else
  echo "Configurazione già esistente: $CONFIG_FILE"
fi